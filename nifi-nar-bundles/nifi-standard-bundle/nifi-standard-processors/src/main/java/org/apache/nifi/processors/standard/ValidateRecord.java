/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.nifi.processors.standard;


import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;
import org.apache.nifi.annotation.behavior.EventDriven;
import org.apache.nifi.annotation.behavior.InputRequirement;
import org.apache.nifi.annotation.behavior.InputRequirement.Requirement;
import org.apache.nifi.annotation.behavior.SideEffectFree;
import org.apache.nifi.annotation.behavior.SupportsBatching;
import org.apache.nifi.annotation.behavior.WritesAttribute;
import org.apache.nifi.annotation.behavior.WritesAttributes;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.avro.AvroSchemaValidator;
import org.apache.nifi.avro.AvroTypeUtil;
import org.apache.nifi.components.AllowableValue;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.components.ValidationContext;
import org.apache.nifi.components.ValidationResult;
import org.apache.nifi.expression.ExpressionLanguageScope;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.flowfile.attributes.CoreAttributes;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.util.StandardValidators;
import org.apache.nifi.schema.access.SchemaField;
import org.apache.nifi.schema.access.SchemaNotFoundException;
import org.apache.nifi.schema.validation.SchemaValidationContext;
import org.apache.nifi.schema.validation.StandardSchemaValidator;
import org.apache.nifi.schemaregistry.services.SchemaRegistry;
import org.apache.nifi.serialization.MalformedRecordException;
import org.apache.nifi.serialization.RecordReader;
import org.apache.nifi.serialization.RecordReaderFactory;
import org.apache.nifi.serialization.RecordSetWriter;
import org.apache.nifi.serialization.RecordSetWriterFactory;
import org.apache.nifi.serialization.WriteResult;
import org.apache.nifi.serialization.record.RawRecordWriter;
import org.apache.nifi.serialization.record.Record;
import org.apache.nifi.serialization.record.RecordSchema;
import org.apache.nifi.serialization.record.SchemaIdentifier;
import org.apache.nifi.serialization.record.validation.RecordSchemaValidator;
import org.apache.nifi.serialization.record.validation.SchemaValidationResult;
import org.apache.nifi.serialization.record.validation.ValidationError;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@EventDriven
@SideEffectFree
@SupportsBatching
@InputRequirement(Requirement.INPUT_REQUIRED)
@Tags({"record", "schema", "validate"})
@CapabilityDescription("Validates the Records of an incoming FlowFile against a given schema. All records that adhere to the schema are routed to the \"valid\" relationship while "
    + "records that do not adhere to the schema are routed to the \"invalid\" relationship. It is therefore possible for a single incoming FlowFile to be split into two individual "
    + "FlowFiles if some records are valid according to the schema and others are not. Any FlowFile that is routed to the \"invalid\" relationship will emit a ROUTE Provenance Event "
    + "with the Details field populated to explain why records were invalid. In addition, to gain further explanation of why records were invalid, DEBUG-level logging can be enabled "
    + "for the \"org.apache.nifi.processors.standard.ValidateRecord\" logger.")
@WritesAttributes({
    @WritesAttribute(attribute = "mime.type", description = "Sets the mime.type attribute to the MIME Type specified by the Record Writer"),
    @WritesAttribute(attribute = "record.count", description = "The number of records in the FlowFile routed to a relationship")
})
public class ValidateRecord extends AbstractProcessor {

    static final AllowableValue SCHEMA_NAME_PROPERTY = new AllowableValue("schema-name-property", "Use Schema Name Property",
        "The schema to validate the data against is determined by looking at the 'Schema Name' Property and looking up the schema in the configured Schema Registry");
    static final AllowableValue SCHEMA_TEXT_PROPERTY = new AllowableValue("schema-text-property", "Use Schema Text Property",
        "The schema to validate the data against is determined by looking at the 'Schema Text' Property and parsing the schema as an Avro schema");
    static final AllowableValue READER_SCHEMA = new AllowableValue("reader-schema", "Use Reader's Schema",
        "The schema to validate the data against is determined by asking the configured Record Reader for its schema");

    static final PropertyDescriptor RECORD_READER = new PropertyDescriptor.Builder()
        .name("record-reader")
        .displayName("Record Reader")
        .description("Specifies the Controller Service to use for reading incoming data")
        .identifiesControllerService(RecordReaderFactory.class)
        .required(true)
        .build();
    static final PropertyDescriptor RECORD_WRITER = new PropertyDescriptor.Builder()
        .name("record-writer")
        .displayName("Record Writer")
        .description("Specifies the Controller Service to use for writing out the records")
        .identifiesControllerService(RecordSetWriterFactory.class)
        .required(true)
        .build();
    static final PropertyDescriptor INVALID_RECORD_WRITER = new PropertyDescriptor.Builder()
        .name("invalid-record-writer")
        .displayName("Record Writer for Invalid Records")
        .description("If specified, this Controller Service will be used to write out any records that are invalid. "
            + "If not specified, the writer specified by the \"Record Writer\" property will be used. This is useful, for example, when the configured "
            + "Record Writer cannot write data that does not adhere to its schema (as is the case with Avro) or when it is desirable to keep invalid records "
            + "in their original format while converting valid records to another format.")
        .identifiesControllerService(RecordSetWriterFactory.class)
        .required(false)
        .build();
    static final PropertyDescriptor SCHEMA_ACCESS_STRATEGY = new PropertyDescriptor.Builder()
        .name("schema-access-strategy")
        .displayName("Schema Access Strategy")
        .description("Specifies how to obtain the schema that should be used to validate records")
        .allowableValues(READER_SCHEMA, SCHEMA_NAME_PROPERTY, SCHEMA_TEXT_PROPERTY)
        .defaultValue(READER_SCHEMA.getValue())
        .required(true)
        .build();
    public static final PropertyDescriptor SCHEMA_REGISTRY = new PropertyDescriptor.Builder()
        .name("schema-registry")
        .displayName("Schema Registry")
        .description("Specifies the Controller Service to use for the Schema Registry. This is necessary only if the Schema Access Strategy is set to \"Use 'Schema Name' Property\".")
        .identifiesControllerService(SchemaRegistry.class)
        .required(false)
        .build();
    static final PropertyDescriptor SCHEMA_NAME = new PropertyDescriptor.Builder()
        .name("schema-name")
        .displayName("Schema Name")
        .description("Specifies the name of the schema to lookup in the Schema Registry property")
        .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
        .expressionLanguageSupported(ExpressionLanguageScope.FLOWFILE_ATTRIBUTES)
        .defaultValue("${schema.name}")
        .required(false)
        .build();
    static final PropertyDescriptor SCHEMA_TEXT = new PropertyDescriptor.Builder()
        .name("schema-text")
        .displayName("Schema Text")
        .description("The text of an Avro-formatted Schema")
        .addValidator(new AvroSchemaValidator())
        .expressionLanguageSupported(ExpressionLanguageScope.FLOWFILE_ATTRIBUTES)
        .defaultValue("${avro.schema}")
        .required(false)
        .build();
    static final PropertyDescriptor ALLOW_EXTRA_FIELDS = new PropertyDescriptor.Builder()
        .name("allow-extra-fields")
        .displayName("Allow Extra Fields")
        .description("If the incoming data has fields that are not present in the schema, this property determines whether or not the Record is valid. "
            + "If true, the Record is still valid. If false, the Record will be invalid due to the extra fields.")
        .expressionLanguageSupported(false)
        .allowableValues("true", "false")
        .defaultValue("true")
        .required(true)
        .build();
    static final PropertyDescriptor STRICT_TYPE_CHECKING = new PropertyDescriptor.Builder()
        .name("strict-type-checking")
        .displayName("Strict Type Checking")
        .description("If the incoming data has a Record where a field is not of the correct type, this property determine whether how to handle the Record. "
            + "If true, the Record will still be considered invalid. If false, the Record will be considered valid and the field will be coerced into the "
            + "correct type (if possible, according to the type coercion supported by the Record Writer).")
        .expressionLanguageSupported(false)
        .allowableValues("true", "false")
        .defaultValue("true")
        .required(true)
        .build();

    static final Relationship REL_VALID = new Relationship.Builder()
        .name("valid")
        .description("Records that are valid according to the schema will be routed to this relationship")
        .build();
    static final Relationship REL_INVALID = new Relationship.Builder()
        .name("invalid")
        .description("Records that are not valid according to the schema will be routed to this relationship")
        .build();
    static final Relationship REL_FAILURE = new Relationship.Builder()
        .name("failure")
        .description("If the records cannot be read, validated, or written, for any reason, the original FlowFile will be routed to this relationship")
        .build();


    @Override
    protected List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        final List<PropertyDescriptor> properties = new ArrayList<>();
        properties.add(RECORD_READER);
        properties.add(RECORD_WRITER);
        properties.add(INVALID_RECORD_WRITER);
        properties.add(SCHEMA_ACCESS_STRATEGY);
        properties.add(SCHEMA_REGISTRY);
        properties.add(SCHEMA_NAME);
        properties.add(SCHEMA_TEXT);
        properties.add(ALLOW_EXTRA_FIELDS);
        properties.add(STRICT_TYPE_CHECKING);
        return properties;
    }

    @Override
    public Set<Relationship> getRelationships() {
        final Set<Relationship> relationships = new HashSet<>();
        relationships.add(REL_VALID);
        relationships.add(REL_INVALID);
        relationships.add(REL_FAILURE);
        return relationships;
    }

    @Override
    protected Collection<ValidationResult> customValidate(final ValidationContext validationContext) {
        final String schemaAccessStrategy = validationContext.getProperty(SCHEMA_ACCESS_STRATEGY).getValue();
        if (schemaAccessStrategy.equals(SCHEMA_NAME_PROPERTY.getValue())) {
            if (!validationContext.getProperty(SCHEMA_REGISTRY).isSet()) {
                return Collections.singleton(new ValidationResult.Builder()
                    .subject("Schema Registry")
                    .valid(false)
                    .explanation("If the Schema Access Strategy is set to \"Use 'Schema Name' Property\", the Schema Registry property must also be set")
                    .build());
            }

            final SchemaRegistry registry = validationContext.getProperty(SCHEMA_REGISTRY).asControllerService(SchemaRegistry.class);
            if (!registry.getSuppliedSchemaFields().contains(SchemaField.SCHEMA_NAME)) {
                return Collections.singleton(new ValidationResult.Builder()
                    .subject("Schema Registry")
                    .valid(false)
                    .explanation("The configured Schema Registry does not support accessing schemas by name")
                    .build());
            }
        }

        return Collections.emptyList();
    }


    @Override
    public void onTrigger(final ProcessContext context, final ProcessSession session) throws ProcessException {
        FlowFile flowFile = session.get();
        if (flowFile == null) {
            return;
        }

        final RecordSetWriterFactory validRecordWriterFactory = context.getProperty(RECORD_WRITER).asControllerService(RecordSetWriterFactory.class);
        final RecordSetWriterFactory invalidRecordWriterFactory = context.getProperty(INVALID_RECORD_WRITER).isSet()
            ? context.getProperty(INVALID_RECORD_WRITER).asControllerService(RecordSetWriterFactory.class)
            : validRecordWriterFactory;
        final RecordReaderFactory readerFactory = context.getProperty(RECORD_READER).asControllerService(RecordReaderFactory.class);

        final boolean allowExtraFields = context.getProperty(ALLOW_EXTRA_FIELDS).asBoolean();
        final boolean strictTypeChecking = context.getProperty(STRICT_TYPE_CHECKING).asBoolean();

        RecordSetWriter validWriter = null;
        RecordSetWriter invalidWriter = null;
        FlowFile validFlowFile = null;
        FlowFile invalidFlowFile = null;

        try (final InputStream in = session.read(flowFile);
            final RecordReader reader = readerFactory.createRecordReader(flowFile, in, getLogger())) {

            final RecordSchema validationSchema = getValidationSchema(context, flowFile, reader);
            final SchemaValidationContext validationContext = new SchemaValidationContext(validationSchema, allowExtraFields, strictTypeChecking);
            final RecordSchemaValidator validator = new StandardSchemaValidator(validationContext);

            int recordCount = 0;
            int validCount = 0;
            int invalidCount = 0;

            final Set<String> extraFields = new HashSet<>();
            final Set<String> missingFields = new HashSet<>();
            final Set<String> invalidFields = new HashSet<>();
            final Set<String> otherProblems = new HashSet<>();

            try {
                Record record;
                while ((record = reader.nextRecord(false, false)) != null) {
                    final SchemaValidationResult result = validator.validate(record);
                    recordCount++;

                    RecordSetWriter writer;
                    if (result.isValid()) {
                        validCount++;
                        if (validFlowFile == null) {
                            validFlowFile = session.create(flowFile);
                        }

                        validWriter = writer = createIfNecessary(validWriter, validRecordWriterFactory, session, validFlowFile, record.getSchema());
                    } else {
                        invalidCount++;
                        logValidationErrors(flowFile, recordCount, result);

                        if (invalidFlowFile == null) {
                            invalidFlowFile = session.create(flowFile);
                        }

                        invalidWriter = writer = createIfNecessary(invalidWriter, invalidRecordWriterFactory, session, invalidFlowFile, record.getSchema());

                        // Add all of the validation errors to our Set<ValidationError> but only keep up to MAX_VALIDATION_ERRORS because if
                        // we keep too many then we both use up a lot of heap and risk outputting so much information in the Provenance Event
                        // that it is too noisy to be useful.
                        for (final ValidationError validationError : result.getValidationErrors()) {
                            final Optional<String> fieldName = validationError.getFieldName();

                            switch (validationError.getType()) {
                                case EXTRA_FIELD:
                                    if (fieldName.isPresent()) {
                                        extraFields.add(fieldName.get());
                                    } else {
                                        otherProblems.add(validationError.getExplanation());
                                    }
                                    break;
                                case MISSING_FIELD:
                                    if (fieldName.isPresent()) {
                                        missingFields.add(fieldName.get());
                                    } else {
                                        otherProblems.add(validationError.getExplanation());
                                    }
                                    break;
                                case INVALID_FIELD:
                                    if (fieldName.isPresent()) {
                                        invalidFields.add(fieldName.get());
                                    } else {
                                        otherProblems.add(validationError.getExplanation());
                                    }
                                    break;
                                case OTHER:
                                    otherProblems.add(validationError.getExplanation());
                                    break;
                            }
                        }
                    }

                    if (writer instanceof RawRecordWriter) {
                        ((RawRecordWriter) writer).writeRawRecord(record);
                    } else {
                        writer.write(record);
                    }
                }

                if (validWriter != null) {
                    completeFlowFile(session, validFlowFile, validWriter, REL_VALID, null);
                }

                if (invalidWriter != null) {
                    // Build up a String that explains why the records were invalid, so that we can add this to the Provenance Event.
                    final StringBuilder errorBuilder = new StringBuilder();
                    errorBuilder.append("Records in this FlowFile were invalid for the following reasons: ");
                    if (!missingFields.isEmpty()) {
                        errorBuilder.append("The following ").append(missingFields.size()).append(" fields were missing: ").append(missingFields.toString());
                    }

                    if (!extraFields.isEmpty()) {
                        if (errorBuilder.length() > 0) {
                            errorBuilder.append("; ");
                        }

                        errorBuilder.append("The following ").append(extraFields.size())
                            .append(" fields were present in the Record but not in the schema: ").append(extraFields.toString());
                    }

                    if (!invalidFields.isEmpty()) {
                        if (errorBuilder.length() > 0) {
                            errorBuilder.append("; ");
                        }

                        errorBuilder.append("The following ").append(invalidFields.size())
                            .append(" fields had values whose type did not match the schema: ").append(invalidFields.toString());
                    }

                    if (!otherProblems.isEmpty()) {
                        if (errorBuilder.length() > 0) {
                            errorBuilder.append("; ");
                        }

                        errorBuilder.append("The following ").append(otherProblems.size())
                            .append(" additional problems were encountered: ").append(otherProblems.toString());
                    }

                    final String validationErrorString = errorBuilder.toString();
                    completeFlowFile(session, invalidFlowFile, invalidWriter, REL_INVALID, validationErrorString);
                }
            } finally {
                closeQuietly(validWriter);
                closeQuietly(invalidWriter);
            }

            session.adjustCounter("Records Validated", recordCount, false);
            session.adjustCounter("Records Found Valid", validCount, false);
            session.adjustCounter("Records Found Invalid", invalidCount, false);
        } catch (final Exception e) {
            getLogger().error("Failed to process {}; will route to failure", new Object[] {flowFile, e});
            session.transfer(flowFile, REL_FAILURE);
            if (validFlowFile != null) {
                session.remove(validFlowFile);
            }
            if (invalidFlowFile != null) {
                session.remove(invalidFlowFile);
            }
            return;
        }

        session.remove(flowFile);
    }

    private void closeQuietly(final RecordSetWriter writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (final Exception e) {
                getLogger().error("Failed to close Record Writer", e);
            }
        }
    }

    private void completeFlowFile(final ProcessSession session, final FlowFile flowFile, final RecordSetWriter writer, final Relationship relationship, final String details) throws IOException {
        final WriteResult writeResult = writer.finishRecordSet();
        writer.close();

        final Map<String, String> attributes = new HashMap<>();
        attributes.putAll(writeResult.getAttributes());
        attributes.put("record.count", String.valueOf(writeResult.getRecordCount()));
        attributes.put(CoreAttributes.MIME_TYPE.key(), writer.getMimeType());
        session.putAllAttributes(flowFile, attributes);

        session.transfer(flowFile, relationship);
        session.getProvenanceReporter().route(flowFile, relationship, details);
    }

    private RecordSetWriter createIfNecessary(final RecordSetWriter writer, final RecordSetWriterFactory factory, final ProcessSession session,
        final FlowFile flowFile, final RecordSchema inputSchema) throws SchemaNotFoundException, IOException {
        if (writer != null) {
            return writer;
        }

        final OutputStream out = session.write(flowFile);
        final RecordSetWriter created = factory.createWriter(getLogger(), inputSchema, out);
        created.beginRecordSet();
        return created;
    }

    private void logValidationErrors(final FlowFile flowFile, final int recordCount, final SchemaValidationResult result) {
        if (getLogger().isDebugEnabled()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("For ").append(flowFile).append(" Record #").append(recordCount).append(" is invalid due to:\n");
            for (final ValidationError error : result.getValidationErrors()) {
                sb.append(error).append("\n");
            }

            getLogger().debug(sb.toString());
        }
    }

    protected RecordSchema getValidationSchema(final ProcessContext context, final FlowFile flowFile, final RecordReader reader)
        throws MalformedRecordException, IOException, SchemaNotFoundException {
        final String schemaAccessStrategy = context.getProperty(SCHEMA_ACCESS_STRATEGY).getValue();
        if (schemaAccessStrategy.equals(READER_SCHEMA.getValue())) {
            return reader.getSchema();
        } else if (schemaAccessStrategy.equals(SCHEMA_NAME_PROPERTY.getValue())) {
            final SchemaRegistry schemaRegistry = context.getProperty(SCHEMA_REGISTRY).asControllerService(SchemaRegistry.class);
            final String schemaName = context.getProperty(SCHEMA_NAME).evaluateAttributeExpressions(flowFile).getValue();
            final SchemaIdentifier schemaIdentifier = SchemaIdentifier.builder().name(schemaName).build();
            return schemaRegistry.retrieveSchema(schemaIdentifier);
        } else if (schemaAccessStrategy.equals(SCHEMA_TEXT_PROPERTY.getValue())) {
            final String schemaText = context.getProperty(SCHEMA_TEXT).evaluateAttributeExpressions(flowFile).getValue();
            final Parser parser = new Schema.Parser();
            final Schema avroSchema = parser.parse(schemaText);
            return AvroTypeUtil.createSchema(avroSchema);
        } else {
            throw new ProcessException("Invalid Schema Access Strategy: " + schemaAccessStrategy);
        }
    }
}
