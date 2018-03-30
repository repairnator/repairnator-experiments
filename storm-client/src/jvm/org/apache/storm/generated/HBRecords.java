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
public class HBRecords implements org.apache.thrift.TBase<HBRecords, HBRecords._Fields>, java.io.Serializable, Cloneable, Comparable<HBRecords> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("HBRecords");

  private static final org.apache.thrift.protocol.TField PULSES_FIELD_DESC = new org.apache.thrift.protocol.TField("pulses", org.apache.thrift.protocol.TType.LIST, (short)1);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new HBRecordsStandardSchemeFactory());
    schemes.put(TupleScheme.class, new HBRecordsTupleSchemeFactory());
  }

  private List<HBPulse> pulses; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    PULSES((short)1, "pulses");

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
        case 1: // PULSES
          return PULSES;
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
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.PULSES, new org.apache.thrift.meta_data.FieldMetaData("pulses", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, HBPulse.class))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(HBRecords.class, metaDataMap);
  }

  public HBRecords() {
  }

  public HBRecords(
    List<HBPulse> pulses)
  {
    this();
    this.pulses = pulses;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public HBRecords(HBRecords other) {
    if (other.is_set_pulses()) {
      List<HBPulse> __this__pulses = new ArrayList<HBPulse>(other.pulses.size());
      for (HBPulse other_element : other.pulses) {
        __this__pulses.add(new HBPulse(other_element));
      }
      this.pulses = __this__pulses;
    }
  }

  public HBRecords deepCopy() {
    return new HBRecords(this);
  }

  @Override
  public void clear() {
    this.pulses = null;
  }

  public int get_pulses_size() {
    return (this.pulses == null) ? 0 : this.pulses.size();
  }

  public java.util.Iterator<HBPulse> get_pulses_iterator() {
    return (this.pulses == null) ? null : this.pulses.iterator();
  }

  public void add_to_pulses(HBPulse elem) {
    if (this.pulses == null) {
      this.pulses = new ArrayList<HBPulse>();
    }
    this.pulses.add(elem);
  }

  public List<HBPulse> get_pulses() {
    return this.pulses;
  }

  public void set_pulses(List<HBPulse> pulses) {
    this.pulses = pulses;
  }

  public void unset_pulses() {
    this.pulses = null;
  }

  /** Returns true if field pulses is set (has been assigned a value) and false otherwise */
  public boolean is_set_pulses() {
    return this.pulses != null;
  }

  public void set_pulses_isSet(boolean value) {
    if (!value) {
      this.pulses = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case PULSES:
      if (value == null) {
        unset_pulses();
      } else {
        set_pulses((List<HBPulse>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case PULSES:
      return get_pulses();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case PULSES:
      return is_set_pulses();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof HBRecords)
      return this.equals((HBRecords)that);
    return false;
  }

  public boolean equals(HBRecords that) {
    if (that == null)
      return false;

    boolean this_present_pulses = true && this.is_set_pulses();
    boolean that_present_pulses = true && that.is_set_pulses();
    if (this_present_pulses || that_present_pulses) {
      if (!(this_present_pulses && that_present_pulses))
        return false;
      if (!this.pulses.equals(that.pulses))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_pulses = true && (is_set_pulses());
    list.add(present_pulses);
    if (present_pulses)
      list.add(pulses);

    return list.hashCode();
  }

  @Override
  public int compareTo(HBRecords other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(is_set_pulses()).compareTo(other.is_set_pulses());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (is_set_pulses()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.pulses, other.pulses);
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
    StringBuilder sb = new StringBuilder("HBRecords(");
    boolean first = true;

    sb.append("pulses:");
    if (this.pulses == null) {
      sb.append("null");
    } else {
      sb.append(this.pulses);
    }
    first = false;
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
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class HBRecordsStandardSchemeFactory implements SchemeFactory {
    public HBRecordsStandardScheme getScheme() {
      return new HBRecordsStandardScheme();
    }
  }

  private static class HBRecordsStandardScheme extends StandardScheme<HBRecords> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, HBRecords struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // PULSES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list904 = iprot.readListBegin();
                struct.pulses = new ArrayList<HBPulse>(_list904.size);
                HBPulse _elem905;
                for (int _i906 = 0; _i906 < _list904.size; ++_i906)
                {
                  _elem905 = new HBPulse();
                  _elem905.read(iprot);
                  struct.pulses.add(_elem905);
                }
                iprot.readListEnd();
              }
              struct.set_pulses_isSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, HBRecords struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.pulses != null) {
        oprot.writeFieldBegin(PULSES_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.pulses.size()));
          for (HBPulse _iter907 : struct.pulses)
          {
            _iter907.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class HBRecordsTupleSchemeFactory implements SchemeFactory {
    public HBRecordsTupleScheme getScheme() {
      return new HBRecordsTupleScheme();
    }
  }

  private static class HBRecordsTupleScheme extends TupleScheme<HBRecords> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, HBRecords struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.is_set_pulses()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.is_set_pulses()) {
        {
          oprot.writeI32(struct.pulses.size());
          for (HBPulse _iter908 : struct.pulses)
          {
            _iter908.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, HBRecords struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list909 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.pulses = new ArrayList<HBPulse>(_list909.size);
          HBPulse _elem910;
          for (int _i911 = 0; _i911 < _list909.size; ++_i911)
          {
            _elem910 = new HBPulse();
            _elem910.read(iprot);
            struct.pulses.add(_elem910);
          }
        }
        struct.set_pulses_isSet(true);
      }
    }
  }

}

