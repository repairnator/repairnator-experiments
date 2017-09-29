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
public class KillOptions implements org.apache.thrift.TBase<KillOptions, KillOptions._Fields>, java.io.Serializable, Cloneable, Comparable<KillOptions> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("KillOptions");

  private static final org.apache.thrift.protocol.TField WAIT_SECS_FIELD_DESC = new org.apache.thrift.protocol.TField("wait_secs", org.apache.thrift.protocol.TType.I32, (short)1);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new KillOptionsStandardSchemeFactory());
    schemes.put(TupleScheme.class, new KillOptionsTupleSchemeFactory());
  }

  private int wait_secs; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    WAIT_SECS((short)1, "wait_secs");

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
        case 1: // WAIT_SECS
          return WAIT_SECS;
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
  private static final int __WAIT_SECS_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.WAIT_SECS};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.WAIT_SECS, new org.apache.thrift.meta_data.FieldMetaData("wait_secs", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(KillOptions.class, metaDataMap);
  }

  public KillOptions() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public KillOptions(KillOptions other) {
    __isset_bitfield = other.__isset_bitfield;
    this.wait_secs = other.wait_secs;
  }

  public KillOptions deepCopy() {
    return new KillOptions(this);
  }

  @Override
  public void clear() {
    set_wait_secs_isSet(false);
    this.wait_secs = 0;
  }

  public int get_wait_secs() {
    return this.wait_secs;
  }

  public void set_wait_secs(int wait_secs) {
    this.wait_secs = wait_secs;
    set_wait_secs_isSet(true);
  }

  public void unset_wait_secs() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __WAIT_SECS_ISSET_ID);
  }

  /** Returns true if field wait_secs is set (has been assigned a value) and false otherwise */
  public boolean is_set_wait_secs() {
    return EncodingUtils.testBit(__isset_bitfield, __WAIT_SECS_ISSET_ID);
  }

  public void set_wait_secs_isSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __WAIT_SECS_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case WAIT_SECS:
      if (value == null) {
        unset_wait_secs();
      } else {
        set_wait_secs((Integer)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case WAIT_SECS:
      return get_wait_secs();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case WAIT_SECS:
      return is_set_wait_secs();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof KillOptions)
      return this.equals((KillOptions)that);
    return false;
  }

  public boolean equals(KillOptions that) {
    if (that == null)
      return false;

    boolean this_present_wait_secs = true && this.is_set_wait_secs();
    boolean that_present_wait_secs = true && that.is_set_wait_secs();
    if (this_present_wait_secs || that_present_wait_secs) {
      if (!(this_present_wait_secs && that_present_wait_secs))
        return false;
      if (this.wait_secs != that.wait_secs)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_wait_secs = true && (is_set_wait_secs());
    list.add(present_wait_secs);
    if (present_wait_secs)
      list.add(wait_secs);

    return list.hashCode();
  }

  @Override
  public int compareTo(KillOptions other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(is_set_wait_secs()).compareTo(other.is_set_wait_secs());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (is_set_wait_secs()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.wait_secs, other.wait_secs);
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
    StringBuilder sb = new StringBuilder("KillOptions(");
    boolean first = true;

    if (is_set_wait_secs()) {
      sb.append("wait_secs:");
      sb.append(this.wait_secs);
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

  private static class KillOptionsStandardSchemeFactory implements SchemeFactory {
    public KillOptionsStandardScheme getScheme() {
      return new KillOptionsStandardScheme();
    }
  }

  private static class KillOptionsStandardScheme extends StandardScheme<KillOptions> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, KillOptions struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // WAIT_SECS
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.wait_secs = iprot.readI32();
              struct.set_wait_secs_isSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, KillOptions struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.is_set_wait_secs()) {
        oprot.writeFieldBegin(WAIT_SECS_FIELD_DESC);
        oprot.writeI32(struct.wait_secs);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class KillOptionsTupleSchemeFactory implements SchemeFactory {
    public KillOptionsTupleScheme getScheme() {
      return new KillOptionsTupleScheme();
    }
  }

  private static class KillOptionsTupleScheme extends TupleScheme<KillOptions> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, KillOptions struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.is_set_wait_secs()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.is_set_wait_secs()) {
        oprot.writeI32(struct.wait_secs);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, KillOptions struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        struct.wait_secs = iprot.readI32();
        struct.set_wait_secs_isSet(true);
      }
    }
  }

}

