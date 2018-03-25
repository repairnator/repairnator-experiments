/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.apache.storm.generated;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)")
public class DebugOptions implements org.apache.thrift.TBase<DebugOptions, DebugOptions._Fields>, java.io.Serializable, Cloneable, Comparable<DebugOptions> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("DebugOptions");

  private static final org.apache.thrift.protocol.TField ENABLE_FIELD_DESC = new org.apache.thrift.protocol.TField("enable", org.apache.thrift.protocol.TType.BOOL, (short)1);
  private static final org.apache.thrift.protocol.TField SAMPLINGPCT_FIELD_DESC = new org.apache.thrift.protocol.TField("samplingpct", org.apache.thrift.protocol.TType.DOUBLE, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new DebugOptionsStandardSchemeFactory());
    schemes.put(TupleScheme.class, new DebugOptionsTupleSchemeFactory());
  }

  private boolean enable; // optional
  private double samplingpct; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    ENABLE((short)1, "enable"),
    SAMPLINGPCT((short)2, "samplingpct");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // ENABLE
          return ENABLE;
        case 2: // SAMPLINGPCT
          return SAMPLINGPCT;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __ENABLE_ISSET_ID = 0;
  private static final int __SAMPLINGPCT_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.ENABLE,_Fields.SAMPLINGPCT};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.ENABLE, new org.apache.thrift.meta_data.FieldMetaData("enable", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    tmpMap.put(_Fields.SAMPLINGPCT, new org.apache.thrift.meta_data.FieldMetaData("samplingpct", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.DOUBLE)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(DebugOptions.class, metaDataMap);
  }

  public DebugOptions() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public DebugOptions(DebugOptions other) {
    __isset_bitfield = other.__isset_bitfield;
    this.enable = other.enable;
    this.samplingpct = other.samplingpct;
  }

  public DebugOptions deepCopy() {
    return new DebugOptions(this);
  }

  @Override
  public void clear() {
    set_enable_isSet(false);
    this.enable = false;
    set_samplingpct_isSet(false);
    this.samplingpct = 0.0;
  }

  public boolean is_enable() {
    return this.enable;
  }

  public void set_enable(boolean enable) {
    this.enable = enable;
    set_enable_isSet(true);
  }

  public void unset_enable() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __ENABLE_ISSET_ID);
  }

  /** Returns true if field enable is set (has been assigned a value) and false otherwise */
  public boolean is_set_enable() {
    return EncodingUtils.testBit(__isset_bitfield, __ENABLE_ISSET_ID);
  }

  public void set_enable_isSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __ENABLE_ISSET_ID, value);
  }

  public double get_samplingpct() {
    return this.samplingpct;
  }

  public void set_samplingpct(double samplingpct) {
    this.samplingpct = samplingpct;
    set_samplingpct_isSet(true);
  }

  public void unset_samplingpct() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __SAMPLINGPCT_ISSET_ID);
  }

  /** Returns true if field samplingpct is set (has been assigned a value) and false otherwise */
  public boolean is_set_samplingpct() {
    return EncodingUtils.testBit(__isset_bitfield, __SAMPLINGPCT_ISSET_ID);
  }

  public void set_samplingpct_isSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __SAMPLINGPCT_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case ENABLE:
      if (value == null) {
        unset_enable();
      } else {
        set_enable((Boolean)value);
      }
      break;

    case SAMPLINGPCT:
      if (value == null) {
        unset_samplingpct();
      } else {
        set_samplingpct((Double)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case ENABLE:
      return is_enable();

    case SAMPLINGPCT:
      return get_samplingpct();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case ENABLE:
      return is_set_enable();
    case SAMPLINGPCT:
      return is_set_samplingpct();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof DebugOptions)
      return this.equals((DebugOptions)that);
    return false;
  }

  public boolean equals(DebugOptions that) {
    if (that == null)
      return false;

    boolean this_present_enable = true && this.is_set_enable();
    boolean that_present_enable = true && that.is_set_enable();
    if (this_present_enable || that_present_enable) {
      if (!(this_present_enable && that_present_enable))
        return false;
      if (this.enable != that.enable)
        return false;
    }

    boolean this_present_samplingpct = true && this.is_set_samplingpct();
    boolean that_present_samplingpct = true && that.is_set_samplingpct();
    if (this_present_samplingpct || that_present_samplingpct) {
      if (!(this_present_samplingpct && that_present_samplingpct))
        return false;
      if (this.samplingpct != that.samplingpct)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_enable = true && (is_set_enable());
    list.add(present_enable);
    if (present_enable)
      list.add(enable);

    boolean present_samplingpct = true && (is_set_samplingpct());
    list.add(present_samplingpct);
    if (present_samplingpct)
      list.add(samplingpct);

    return list.hashCode();
  }

  @Override
  public int compareTo(DebugOptions other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(is_set_enable()).compareTo(other.is_set_enable());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (is_set_enable()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.enable, other.enable);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(is_set_samplingpct()).compareTo(other.is_set_samplingpct());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (is_set_samplingpct()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.samplingpct, other.samplingpct);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("DebugOptions(");
    boolean first = true;

    if (is_set_enable()) {
      sb.append("enable:");
      sb.append(this.enable);
      first = false;
    }
    if (is_set_samplingpct()) {
      if (!first) sb.append(", ");
      sb.append("samplingpct:");
      sb.append(this.samplingpct);
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class DebugOptionsStandardSchemeFactory implements SchemeFactory {
    public DebugOptionsStandardScheme getScheme() {
      return new DebugOptionsStandardScheme();
    }
  }

  private static class DebugOptionsStandardScheme extends StandardScheme<DebugOptions> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, DebugOptions struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // ENABLE
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.enable = iprot.readBool();
              struct.set_enable_isSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // SAMPLINGPCT
            if (schemeField.type == org.apache.thrift.protocol.TType.DOUBLE) {
              struct.samplingpct = iprot.readDouble();
              struct.set_samplingpct_isSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, DebugOptions struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.is_set_enable()) {
        oprot.writeFieldBegin(ENABLE_FIELD_DESC);
        oprot.writeBool(struct.enable);
        oprot.writeFieldEnd();
      }
      if (struct.is_set_samplingpct()) {
        oprot.writeFieldBegin(SAMPLINGPCT_FIELD_DESC);
        oprot.writeDouble(struct.samplingpct);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class DebugOptionsTupleSchemeFactory implements SchemeFactory {
    public DebugOptionsTupleScheme getScheme() {
      return new DebugOptionsTupleScheme();
    }
  }

  private static class DebugOptionsTupleScheme extends TupleScheme<DebugOptions> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, DebugOptions struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.is_set_enable()) {
        optionals.set(0);
      }
      if (struct.is_set_samplingpct()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.is_set_enable()) {
        oprot.writeBool(struct.enable);
      }
      if (struct.is_set_samplingpct()) {
        oprot.writeDouble(struct.samplingpct);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, DebugOptions struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.enable = iprot.readBool();
        struct.set_enable_isSet(true);
      }
      if (incoming.get(1)) {
        struct.samplingpct = iprot.readDouble();
        struct.set_samplingpct_isSet(true);
      }
    }
  }

}

