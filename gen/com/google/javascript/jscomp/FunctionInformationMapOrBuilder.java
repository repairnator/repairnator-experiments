// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: com/google/javascript/jscomp/function_info.proto

package com.google.javascript.jscomp;

public interface FunctionInformationMapOrBuilder
    extends com.google.protobuf.MessageOrBuilder {

  // repeated group Entry = 1 {
  /**
   * <code>repeated group Entry = 1 { ... }</code>
   */
  java.util.List<com.google.javascript.jscomp.FunctionInformationMap.Entry> 
      getEntryList();
  /**
   * <code>repeated group Entry = 1 { ... }</code>
   */
  com.google.javascript.jscomp.FunctionInformationMap.Entry getEntry(int index);
  /**
   * <code>repeated group Entry = 1 { ... }</code>
   */
  int getEntryCount();
  /**
   * <code>repeated group Entry = 1 { ... }</code>
   */
  java.util.List<? extends com.google.javascript.jscomp.FunctionInformationMap.EntryOrBuilder> 
      getEntryOrBuilderList();
  /**
   * <code>repeated group Entry = 1 { ... }</code>
   */
  com.google.javascript.jscomp.FunctionInformationMap.EntryOrBuilder getEntryOrBuilder(
      int index);

  // repeated group Module = 101 {
  /**
   * <code>repeated group Module = 101 { ... }</code>
   */
  java.util.List<com.google.javascript.jscomp.FunctionInformationMap.Module> 
      getModuleList();
  /**
   * <code>repeated group Module = 101 { ... }</code>
   */
  com.google.javascript.jscomp.FunctionInformationMap.Module getModule(int index);
  /**
   * <code>repeated group Module = 101 { ... }</code>
   */
  int getModuleCount();
  /**
   * <code>repeated group Module = 101 { ... }</code>
   */
  java.util.List<? extends com.google.javascript.jscomp.FunctionInformationMap.ModuleOrBuilder> 
      getModuleOrBuilderList();
  /**
   * <code>repeated group Module = 101 { ... }</code>
   */
  com.google.javascript.jscomp.FunctionInformationMap.ModuleOrBuilder getModuleOrBuilder(
      int index);
}
