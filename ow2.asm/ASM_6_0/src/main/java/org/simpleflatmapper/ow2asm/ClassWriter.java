// ASM: a very small and fast Java bytecode manipulation framework
// Copyright (c) 2000-2011 INRIA, France Telecom
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
//    notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright
//    notice, this list of conditions and the following disclaimer in the
//    documentation and/or other materials provided with the distribution.
// 3. Neither the name of the copyright holders nor the names of its
//    contributors may be used to endorse or promote products derived from
//    this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
// THE POSSIBILITY OF SUCH DAMAGE.
package org.simpleflatmapper.ow2asm;

/**
 * A {@link ClassVisitor} that generates a corresponding ClassFile structure, as defined in the Java
 * Virtual Machine Specification (JVMS). It can be used alone, to generate a Java class "from
 * scratch", or with one or more {@link ClassReader} and adapter {@link ClassVisitor} to generate a
 * modified class from one or more existing Java classes.
 *
 * @see <a href="https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html">JVMS 4</a>
 * @author Eric Bruneton
 */
public class ClassWriter extends ClassVisitor {

  /**
   * Flag to automatically compute the maximum stack size and the maximum number of local variables
   * of methods. If this flag is set, then the arguments of the {@link MethodVisitor#visitMaxs}
   * method of the {@link MethodVisitor} returned by the {@link #visitMethod} method will be
   * ignored, and computed automatically from the signature and the bytecode of each method.
   *
   * @see #ClassWriter(int)
   */
  public static final int COMPUTE_MAXS = 1;

  /**
   * Flag to automatically compute the stack map frames of methods from scratch. If this flag is
   * set, then the calls to the {@link MethodVisitor#visitFrame} method are ignored, and the stack
   * map frames are recomputed from the methods bytecode. The arguments of the {@link
   * MethodVisitor#visitMaxs} method are also ignored and recomputed from the bytecode. In other
   * words, COMPUTE_FRAMES implies COMPUTE_MAXS.
   *
   * @see #ClassWriter(int)
   */
  public static final int COMPUTE_FRAMES = 2;

  // Note: fields are ordered as in the ClassFile structure, and those related to attributes are
  // ordered as in Section 4.7 of the JVMS.

  /**
   * The minor_version and major_version fields of the JVMS ClassFile structure. minor_version is
   * stored in the 16 most significant bits, and major_version in the 16 least significant bits.
   */
  private int version;

  /** The symbol table for this class (contains the constant_pool and the BootstrapMethods). */
  private final SymbolTable symbolTable;

  /**
   * The access_flags field of the JVMS ClassFile structure. This field can contain ASM specific
   * access flags, such as {@link Opcodes#ACC_DEPRECATED}, which are removed when generating the
   * ClassFile structure.
   */
  private int accessFlags;

  /** The this_class field of the JVMS ClassFile structure. */
  private int thisClass;

  /** The super_class field of the JVMS ClassFile structure. */
  private int superClass;

  /** The interface_count field of the JVMS ClassFile structure. */
  private int interfaceCount;

  /** The 'interfaces' array of the JVMS ClassFile structure. */
  private int[] interfaces;

  /**
   * The fields of this class, stored in a linked list of {@link FieldWriter} linked via their
   * {@link FieldWriter#fv} field. This field stores the first element of this list.
   */
  private FieldWriter firstField;

  /**
   * The fields of this class, stored in a linked list of {@link FieldWriter} linked via their
   * {@link FieldWriter#fv} field. This field stores the last element of this list.
   */
  private FieldWriter lastField;

  /**
   * The methods of this class, stored in a linked list of {@link MethodWriter} linked via their
   * {@link MethodWriter#mv} field. This field stores the first element of this list.
   */
  private MethodWriter firstMethod;

  /**
   * The methods of this class, stored in a linked list of {@link MethodWriter} linked via their
   * {@link MethodWriter#mv} field. This field stores the last element of this list.
   */
  private MethodWriter lastMethod;

  /** The number_of_classes field of the InnerClasses attribute, or 0. */
  private int numberOfClasses;

  /** The 'classes' array of the InnerClasses attribute, or <tt>null</tt>. */
  private ByteVector classes;

  /** The class_index field of the EnclosingMethod attribute, or 0. */
  private int enclosingClassIndex;

  /** The method_index field of the EnclosingMethod attribute. */
  private int enclosingMethodIndex;

  /** The signature_index field of the Signature attribute, or 0. */
  private int signatureIndex;

  /** The source_file_index field of the SourceFile attribute, or 0. */
  private int sourceFileIndex;

  /** The debug_extension field of the SourceDebugExtension attribute, or <tt>null</tt>. */
  private ByteVector debugExtension;

  /**
   * The last runtime visible annotation of this class. The previous ones can be accessed with the
   * {@link AnnotationWriter#previousAnnotation} field. May be <tt>null</tt>.
   */
  private AnnotationWriter lastRuntimeVisibleAnnotation;

  /**
   * The last runtime invisible annotation of this class. The previous ones can be accessed with the
   * {@link AnnotationWriter#previousAnnotation} field. May be <tt>null</tt>.
   */
  private AnnotationWriter lastRuntimeInvisibleAnnotation;

  /**
   * The last runtime visible type annotation of this class. The previous ones can be accessed with
   * the {@link AnnotationWriter#previousAnnotation} field. May be <tt>null</tt>.
   */
  private AnnotationWriter lastRuntimeVisibleTypeAnnotation;

  /**
   * The last runtime invisible type annotation of this class. The previous ones can be accessed
   * with the {@link AnnotationWriter#previousAnnotation} field. May be <tt>null</tt>.
   */
  private AnnotationWriter lastRuntimeInvisibleTypeAnnotation;

  /** The Module attribute of this class, or <tt>null</tt>. */
  private ModuleWriter moduleWriter;

  /**
   * The first non standard attribute of this class. The next ones can be accessed with the {@link
   * Attribute#nextAttribute} field. May be <tt>null</tt>.
   *
   * <p><b>WARNING</b>: this list stores the attributes in the <i>reverse</i> order of their visit.
   * firstAttribute is actually the last attribute visited in {@link #visitAttribute}. The {@link
   * #toByteArray} method writes the attributes in the order defined by this list, i.e. in the
   * reverse order specified by the user.
   */
  private Attribute firstAttribute;

  /**
   * Indicates what must be automatically computed in {@link MethodWriter}. Must be one of {@link
   * MethodWriter#COMPUTE_NOTHING}, {@link MethodWriter#COMPUTE_MAX_STACK_AND_LOCAL}, {@link
   * MethodWriter#COMPUTE_INSERTED_FRAMES}, or {@link MethodWriter#COMPUTE_ALL_FRAMES}.
   */
  private int compute;

  // -----------------------------------------------------------------------------------------------
  // Constructor
  // -----------------------------------------------------------------------------------------------

  /**
   * Constructs a new {@link ClassWriter} object.
   *
   * @param flags option flags that can be used to modify the default behavior of this class. Must
   *     be zero or more of {@link #COMPUTE_MAXS} and {@link #COMPUTE_FRAMES}.
   */
  public ClassWriter(final int flags) {
    this(null, flags);
  }

  /**
   * Constructs a new {@link ClassWriter} object and enables optimizations for "mostly add" bytecode
   * transformations. These optimizations are the following:
   *
   * <ul>
   *   <li>The constant pool and bootstrap methods from the original class are copied as is in the
   *       new class, which saves time. New constant pool entries and new bootstrap methods will be
   *       added at the end if necessary, but unused constant pool entries or bootstrap methods
   *       <i>won't be removed</i>.
   *   <li>Methods that are not transformed are copied as is in the new class, directly from the
   *       original class bytecode (i.e. without emitting visit events for all the method
   *       instructions), which saves a <i>lot</i> of time. Untransformed methods are detected by
   *       the fact that the {@link ClassReader} receives {@link MethodVisitor} objects that come
   *       from a {@link ClassWriter} (and not from any other {@link ClassVisitor} instance).
   * </ul>
   *
   * @param classReader the {@link ClassReader} used to read the original class. It will be used to
   *     copy the entire constant pool and bootstrap methods from the original class and also to
   *     copy other fragments of original bytecode where applicable.
   * @param flags option flags that can be used to modify the default behavior of this class.Must be
   *     zero or more of {@link #COMPUTE_MAXS} and {@link #COMPUTE_FRAMES}. <i>These option flags do
   *     not affect methods that are copied as is in the new class. This means that neither the
   *     maximum stack size nor the stack frames will be computed for these methods</i>.
   */
  public ClassWriter(final ClassReader classReader, final int flags) {
    super(Opcodes.ASM6);
    symbolTable = classReader == null ? new SymbolTable(this) : new SymbolTable(this, classReader);
    if ((flags & COMPUTE_FRAMES) != 0) {
      this.compute = MethodWriter.COMPUTE_ALL_FRAMES;
    } else if ((flags & COMPUTE_MAXS) != 0) {
      this.compute = MethodWriter.COMPUTE_MAX_STACK_AND_LOCAL;
    } else {
      this.compute = MethodWriter.COMPUTE_NOTHING;
    }
  }

  // -----------------------------------------------------------------------------------------------
  // Implementation of the ClassVisitor abstract class
  // -----------------------------------------------------------------------------------------------

  @Override
  public final void visit(
      final int version,
      final int access,
      final String name,
      final String signature,
      final String superName,
      final String[] interfaces) {
    this.version = version;
    this.accessFlags = access;
    this.thisClass = symbolTable.setMajorVersionAndClassName(version & 0xFFFF, name);
    if (signature != null) {
      this.signatureIndex = symbolTable.addConstantUtf8(signature);
    }
    this.superClass = superName == null ? 0 : symbolTable.addConstantClass(superName).index;
    if (interfaces != null && interfaces.length > 0) {
      interfaceCount = interfaces.length;
      this.interfaces = new int[interfaceCount];
      for (int i = 0; i < interfaceCount; ++i) {
        this.interfaces[i] = symbolTable.addConstantClass(interfaces[i]).index;
      }
    }
  }

  @Override
  public final void visitSource(final String file, final String debug) {
    if (file != null) {
      sourceFileIndex = symbolTable.addConstantUtf8(file);
    }
    if (debug != null) {
      debugExtension = new ByteVector().encodeUTF8(debug, 0, Integer.MAX_VALUE);
    }
  }

  @Override
  public final ModuleVisitor visitModule(
      final String name, final int access, final String version) {
    return moduleWriter =
        new ModuleWriter(
            symbolTable,
            symbolTable.addConstantModule(name).index,
            access,
            version == null ? 0 : symbolTable.addConstantUtf8(version));
  }

  @Override
  public final void visitOuterClass(final String owner, final String name, final String desc) {
    enclosingClassIndex = symbolTable.addConstantClass(owner).index;
    if (name != null && desc != null) {
      enclosingMethodIndex = symbolTable.addConstantNameAndType(name, desc);
    }
  }

  @Override
  public final AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
    // Create a ByteVector to hold an 'annotation' JVMS structure.
    // See https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html#jvms-4.7.16.
    ByteVector annotation = new ByteVector();
    // Write type_index and reserve space for num_element_value_pairs.
    annotation.putShort(symbolTable.addConstantUtf8(desc)).putShort(0);
    if (visible) {
      return lastRuntimeVisibleAnnotation =
          new AnnotationWriter(symbolTable, annotation, lastRuntimeVisibleAnnotation);
    } else {
      return lastRuntimeInvisibleAnnotation =
          new AnnotationWriter(symbolTable, annotation, lastRuntimeInvisibleAnnotation);
    }
  }

  @Override
  public final AnnotationVisitor visitTypeAnnotation(
      int typeRef, TypePath typePath, final String desc, final boolean visible) {
    // Create a ByteVector to hold a 'type_annotation' JVMS structure.
    // See https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html#jvms-4.7.20.
    ByteVector typeAnnotation = new ByteVector();
    // Write target_type, target_info, and target_path.
    TypeReference.putTarget(typeRef, typeAnnotation);
    TypePath.put(typePath, typeAnnotation);
    // Write type_index and reserve space for num_element_value_pairs.
    typeAnnotation.putShort(symbolTable.addConstantUtf8(desc)).putShort(0);
    if (visible) {
      return lastRuntimeVisibleTypeAnnotation =
          new AnnotationWriter(symbolTable, typeAnnotation, lastRuntimeVisibleTypeAnnotation);
    } else {
      return lastRuntimeInvisibleTypeAnnotation =
          new AnnotationWriter(symbolTable, typeAnnotation, lastRuntimeInvisibleTypeAnnotation);
    }
  }

  @Override
  public final void visitAttribute(final Attribute attribute) {
    // Store the attributes in the <i>reverse</i> order of their visit by this method.
    attribute.nextAttribute = firstAttribute;
    firstAttribute = attribute;
  }

  @Override
  public final void visitInnerClass(
      final String name, final String outerName, final String innerName, final int access) {
    if (classes == null) {
      classes = new ByteVector();
    }
    // Section 4.7.6 of the JVMS states "Every CONSTANT_Class_info entry in the constant_pool table
    // which represents a class or interface C that is not a package member must have exactly one
    // corresponding entry in the classes array". To avoid duplicates we keep track in the info
    // field of the Symbol of each CONSTANT_Class_info entry C whether an inner class entry has
    // already been added for C. If so, we store the index of this inner class entry (plus one) in
    // the info field. This trick allows duplicate detection in O(1) time.
    Symbol nameSymbol = symbolTable.addConstantClass(name);
    if (nameSymbol.info == 0) {
      ++numberOfClasses;
      classes.putShort(nameSymbol.index);
      classes.putShort(outerName == null ? 0 : symbolTable.addConstantClass(outerName).index);
      classes.putShort(innerName == null ? 0 : symbolTable.addConstantUtf8(innerName));
      classes.putShort(access);
      nameSymbol.info = numberOfClasses;
    } else {
      // Compare the inner classes entry nameSymbol.info - 1 with the arguments of this method and
      // throw an exception if there is a difference?
    }
  }

  @Override
  public final FieldVisitor visitField(
      final int access,
      final String name,
      final String desc,
      final String signature,
      final Object value) {
    FieldWriter fieldWriter = new FieldWriter(symbolTable, access, name, desc, signature, value);
    if (firstField == null) {
      firstField = fieldWriter;
    } else {
      lastField.fv = fieldWriter;
    }
    return lastField = fieldWriter;
  }

  @Override
  public final MethodVisitor visitMethod(
      final int access,
      final String name,
      final String desc,
      final String signature,
      final String[] exceptions) {
    MethodWriter methodWriter =
        new MethodWriter(symbolTable, access, name, desc, signature, exceptions, compute);
    if (firstMethod == null) {
      firstMethod = methodWriter;
    } else {
      lastMethod.mv = methodWriter;
    }
    return lastMethod = methodWriter;
  }

  @Override
  public final void visitEnd() {}

  // -----------------------------------------------------------------------------------------------
  // Other public methods
  // -----------------------------------------------------------------------------------------------

  /**
   * Returns the content of the class file that was built by this ClassWriter.
   *
   * @return the binary content of the JVMS ClassFile structure that was built by this ClassWriter.
   */
  public byte[] toByteArray() {
    // First step: compute the size in bytes of the ClassFile structure
    // The magic field uses 4 bytes, 10 mandatory fields (minor_version, major_version,
    // constant_pool_count, access_flags, this_class, super_class, interfaces_count, fields_count,
    // methods_count and attributes_count) use 2 bytes each, and each interface uses 2 bytes too.
    int size = 24 + 2 * interfaceCount;
    int fieldsCount = 0;
    FieldWriter fieldWriter = firstField;
    while (fieldWriter != null) {
      ++fieldsCount;
      size += fieldWriter.computeFieldInfoSize();
      fieldWriter = (FieldWriter) fieldWriter.fv;
    }
    int methodsCount = 0;
    MethodWriter methodWriter = firstMethod;
    while (methodWriter != null) {
      ++methodsCount;
      size += methodWriter.computeMethodInfoSize();
      methodWriter = (MethodWriter) methodWriter.mv;
    }
    // For ease of reference, we use here the same attribute order as in Section 4.7 of the JVMS.
    int attributesCount = 0;
    if (classes != null) {
      ++attributesCount;
      size += 8 + classes.length;
      symbolTable.addConstantUtf8("InnerClasses");
    }
    if (enclosingClassIndex != 0) {
      ++attributesCount;
      size += 10;
      symbolTable.addConstantUtf8("EnclosingMethod");
    }
    if ((accessFlags & Opcodes.ACC_SYNTHETIC) != 0) {
      if ((version & 0xFFFF) < Opcodes.V1_5) {
        ++attributesCount;
        size += 6;
        symbolTable.addConstantUtf8("Synthetic");
      }
    }
    if (signatureIndex != 0) {
      ++attributesCount;
      size += 8;
      symbolTable.addConstantUtf8("Signature");
    }
    if (sourceFileIndex != 0) {
      ++attributesCount;
      size += 8;
      symbolTable.addConstantUtf8("SourceFile");
    }
    if (debugExtension != null) {
      ++attributesCount;
      size += 6 + debugExtension.length;
      symbolTable.addConstantUtf8("SourceDebugExtension");
    }
    if ((accessFlags & Opcodes.ACC_DEPRECATED) != 0) {
      ++attributesCount;
      size += 6;
      symbolTable.addConstantUtf8("Deprecated");
    }
    if (lastRuntimeVisibleAnnotation != null) {
      ++attributesCount;
      size += lastRuntimeVisibleAnnotation.computeAnnotationsSize("RuntimeVisibleAnnotations");
    }
    if (lastRuntimeInvisibleAnnotation != null) {
      ++attributesCount;
      size += lastRuntimeInvisibleAnnotation.computeAnnotationsSize("RuntimeInvisibleAnnotations");
    }
    if (lastRuntimeVisibleTypeAnnotation != null) {
      ++attributesCount;
      size +=
          lastRuntimeVisibleTypeAnnotation.computeAnnotationsSize("RuntimeVisibleTypeAnnotations");
    }
    if (lastRuntimeInvisibleTypeAnnotation != null) {
      ++attributesCount;
      size +=
          lastRuntimeInvisibleTypeAnnotation.computeAnnotationsSize(
              "RuntimeInvisibleTypeAnnotations");
    }
    if (symbolTable.computeBootstrapMethodsSize() > 0) {
      ++attributesCount;
      size += symbolTable.computeBootstrapMethodsSize();
    }
    if (moduleWriter != null) {
      attributesCount += moduleWriter.getAttributeCount();
      size += moduleWriter.computeAttributesSize();
    }
    if (firstAttribute != null) {
      attributesCount += firstAttribute.getAttributeCount();
      size += firstAttribute.getAttributesSize(symbolTable);
    }
    // IMPORTANT: this must be the last part of the ClassFile size computation, because the previous
    // statements can add attribute names to the constant pool, thereby changing its size!
    size += symbolTable.getConstantPoolLength();
    if (symbolTable.getConstantPoolCount() > 0xFFFF) {
      throw new RuntimeException("Class file too large!");
    }

    // Second step: allocate a ByteVector of the correct size (in order to avoid any array copy in
    // dynamic resizes) and fill it with the ClassFile content.
    ByteVector result = new ByteVector(size);
    result.putInt(0xCAFEBABE).putInt(version);
    symbolTable.putConstantPool(result);
    int mask = (version & 0xFFFF) < Opcodes.V1_5 ? Opcodes.ACC_SYNTHETIC : 0;
    result.putShort(accessFlags & ~mask).putShort(thisClass).putShort(superClass);
    result.putShort(interfaceCount);
    for (int i = 0; i < interfaceCount; ++i) {
      result.putShort(interfaces[i]);
    }
    result.putShort(fieldsCount);
    fieldWriter = firstField;
    while (fieldWriter != null) {
      fieldWriter.putFieldInfo(result);
      fieldWriter = (FieldWriter) fieldWriter.fv;
    }
    result.putShort(methodsCount);
    boolean hasFrames = false;
    boolean hasAsmInstructions = false;
    methodWriter = firstMethod;
    while (methodWriter != null) {
      hasFrames |= methodWriter.hasFrames();
      hasAsmInstructions |= methodWriter.hasAsmInstructions();
      methodWriter.putMethodInfo(result);
      methodWriter = (MethodWriter) methodWriter.mv;
    }
    // For ease of reference, we use here the same attribute order as in Section 4.7 of the JVMS.
    result.putShort(attributesCount);
    if (classes != null) {
      result.putShort(symbolTable.addConstantUtf8("InnerClasses"));
      result.putInt(classes.length + 2).putShort(numberOfClasses);
      result.putByteArray(classes.data, 0, classes.length);
    }
    if (enclosingClassIndex != 0) {
      result.putShort(symbolTable.addConstantUtf8("EnclosingMethod")).putInt(4);
      result.putShort(enclosingClassIndex).putShort(enclosingMethodIndex);
    }
    if ((accessFlags & Opcodes.ACC_SYNTHETIC) != 0 && (version & 0xFFFF) < Opcodes.V1_5) {
      result.putShort(symbolTable.addConstantUtf8("Synthetic")).putInt(0);
    }
    if (signatureIndex != 0) {
      result.putShort(symbolTable.addConstantUtf8("Signature")).putInt(2).putShort(signatureIndex);
    }
    if (sourceFileIndex != 0) {
      result
          .putShort(symbolTable.addConstantUtf8("SourceFile"))
          .putInt(2)
          .putShort(sourceFileIndex);
    }
    if (debugExtension != null) {
      int length = debugExtension.length;
      result.putShort(symbolTable.addConstantUtf8("SourceDebugExtension")).putInt(length);
      result.putByteArray(debugExtension.data, 0, length);
    }
    if ((accessFlags & Opcodes.ACC_DEPRECATED) != 0) {
      result.putShort(symbolTable.addConstantUtf8("Deprecated")).putInt(0);
    }
    if (lastRuntimeVisibleAnnotation != null) {
      lastRuntimeVisibleAnnotation.putAnnotations(
          symbolTable.addConstantUtf8("RuntimeVisibleAnnotations"), result);
    }
    if (lastRuntimeInvisibleAnnotation != null) {
      lastRuntimeInvisibleAnnotation.putAnnotations(
          symbolTable.addConstantUtf8("RuntimeInvisibleAnnotations"), result);
    }
    if (lastRuntimeVisibleTypeAnnotation != null) {
      lastRuntimeVisibleTypeAnnotation.putAnnotations(
          symbolTable.addConstantUtf8("RuntimeVisibleTypeAnnotations"), result);
    }
    if (lastRuntimeInvisibleTypeAnnotation != null) {
      lastRuntimeInvisibleTypeAnnotation.putAnnotations(
          symbolTable.addConstantUtf8("RuntimeInvisibleTypeAnnotations"), result);
    }
    symbolTable.putBootstrapMethods(result);
    if (moduleWriter != null) {
      moduleWriter.putAttributes(result);
    }
    if (firstAttribute != null) {
      firstAttribute.putAttributes(symbolTable, result);
    }

    // Third step: do a ClassReader->ClassWriter round trip if the generated class contains ASM
    // specific instructions due to large forward jumps.
    if (hasAsmInstructions) {
      firstField = null;
      lastField = null;
      firstMethod = null;
      lastMethod = null;
      lastRuntimeVisibleAnnotation = null;
      lastRuntimeInvisibleAnnotation = null;
      lastRuntimeVisibleTypeAnnotation = null;
      lastRuntimeInvisibleTypeAnnotation = null;
      moduleWriter = null;
      firstAttribute = null;
      compute = hasFrames ? MethodWriter.COMPUTE_INSERTED_FRAMES : MethodWriter.COMPUTE_NOTHING;
      new ClassReader(result.data)
          .accept(this, (hasFrames ? ClassReader.EXPAND_FRAMES : 0) | ClassReader.EXPAND_ASM_INSNS);
      return toByteArray();
    } else {
      return result.data;
    }
  }

  // -----------------------------------------------------------------------------------------------
  // Utility methods: constant pool management for Attribute sub classes
  // -----------------------------------------------------------------------------------------------

  /**
   * Adds a number or string constant to the constant pool of the class being build. Does nothing if
   * the constant pool already contains a similar item. <i>This method is intended for {@link
   * Attribute} sub classes, and is normally not needed by class generators or adapters.</i>
   *
   * @param cst the value of the constant to be added to the constant pool. This parameter must be
   *     an {@link Integer}, a {@link Float}, a {@link Long}, a {@link Double} or a {@link String}.
   * @return the index of a new or already existing constant item with the given value.
   */
  public int newConst(final Object cst) {
    return symbolTable.addConstant(cst).index;
  }

  /**
   * Adds an UTF8 string to the constant pool of the class being build. Does nothing if the constant
   * pool already contains a similar item. <i>This method is intended for {@link Attribute} sub
   * classes, and is normally not needed by class generators or adapters.</i>
   *
   * @param value the String value.
   * @return the index of a new or already existing UTF8 item.
   */
  public int newUTF8(final String value) {
    return symbolTable.addConstantUtf8(value);
  }

  /**
   * Adds a class reference to the constant pool of the class being build. Does nothing if the
   * constant pool already contains a similar item. <i>This method is intended for {@link Attribute}
   * sub classes, and is normally not needed by class generators or adapters.</i>
   *
   * @param value the internal name of the class.
   * @return the index of a new or already existing class reference item.
   */
  public int newClass(final String value) {
    return symbolTable.addConstantClass(value).index;
  }

  /**
   * Adds a method type reference to the constant pool of the class being build. Does nothing if the
   * constant pool already contains a similar item. <i>This method is intended for {@link Attribute}
   * sub classes, and is normally not needed by class generators or adapters.</i>
   *
   * @param methodDesc method descriptor of the method type.
   * @return the index of a new or already existing method type reference item.
   */
  public int newMethodType(final String methodDesc) {
    return symbolTable.addConstantMethodType(methodDesc).index;
  }

  /**
   * Adds a module reference to the constant pool of the class being build. Does nothing if the
   * constant pool already contains a similar item. <i>This method is intended for {@link Attribute}
   * sub classes, and is normally not needed by class generators or adapters.</i>
   *
   * @param moduleName name of the module.
   * @return the index of a new or already existing module reference item.
   */
  public int newModule(final String moduleName) {
    return symbolTable.addConstantModule(moduleName).index;
  }

  /**
   * Adds a package reference to the constant pool of the class being build. Does nothing if the
   * constant pool already contains a similar item. <i>This method is intended for {@link Attribute}
   * sub classes, and is normally not needed by class generators or adapters.</i>
   *
   * @param packageName name of the package in its internal form.
   * @return the index of a new or already existing module reference item.
   */
  public int newPackage(final String packageName) {
    return symbolTable.addConstantPackage(packageName).index;
  }

  /**
   * Adds a handle to the constant pool of the class being build. Does nothing if the constant pool
   * already contains a similar item. <i>This method is intended for {@link Attribute} sub classes,
   * and is normally not needed by class generators or adapters.</i>
   *
   * @param tag the kind of this handle. Must be {@link Opcodes#H_GETFIELD}, {@link
   *     Opcodes#H_GETSTATIC}, {@link Opcodes#H_PUTFIELD}, {@link Opcodes#H_PUTSTATIC}, {@link
   *     Opcodes#H_INVOKEVIRTUAL}, {@link Opcodes#H_INVOKESTATIC}, {@link Opcodes#H_INVOKESPECIAL},
   *     {@link Opcodes#H_NEWINVOKESPECIAL} or {@link Opcodes#H_INVOKEINTERFACE}.
   * @param owner the internal name of the field or method owner class.
   * @param name the name of the field or method.
   * @param desc the descriptor of the field or method.
   * @return the index of a new or already existing method type reference item.
   * @deprecated this method is superseded by {@link #newHandle(int, String, String, String,
   *     boolean)}.
   */
  @Deprecated
  public int newHandle(final int tag, final String owner, final String name, final String desc) {
    return newHandle(tag, owner, name, desc, tag == Opcodes.H_INVOKEINTERFACE);
  }

  /**
   * Adds a handle to the constant pool of the class being build. Does nothing if the constant pool
   * already contains a similar item. <i>This method is intended for {@link Attribute} sub classes,
   * and is normally not needed by class generators or adapters.</i>
   *
   * @param tag the kind of this handle. Must be {@link Opcodes#H_GETFIELD}, {@link
   *     Opcodes#H_GETSTATIC}, {@link Opcodes#H_PUTFIELD}, {@link Opcodes#H_PUTSTATIC}, {@link
   *     Opcodes#H_INVOKEVIRTUAL}, {@link Opcodes#H_INVOKESTATIC}, {@link Opcodes#H_INVOKESPECIAL},
   *     {@link Opcodes#H_NEWINVOKESPECIAL} or {@link Opcodes#H_INVOKEINTERFACE}.
   * @param owner the internal name of the field or method owner class.
   * @param name the name of the field or method.
   * @param desc the descriptor of the field or method.
   * @param itf true if the owner is an interface.
   * @return the index of a new or already existing method type reference item.
   */
  public int newHandle(
      final int tag, final String owner, final String name, final String desc, final boolean itf) {
    return symbolTable.addConstantMethodHandle(tag, owner, name, desc, itf).index;
  }

  /**
   * Adds an invokedynamic reference to the constant pool of the class being build. Does nothing if
   * the constant pool already contains a similar item. <i>This method is intended for {@link
   * Attribute} sub classes, and is normally not needed by class generators or adapters.</i>
   *
   * @param name name of the invoked method.
   * @param desc descriptor of the invoke method.
   * @param bsm the bootstrap method.
   * @param bsmArgs the bootstrap method constant arguments.
   * @return the index of a new or already existing invokedynamic reference item.
   */
  public int newInvokeDynamic(
      final String name, final String desc, final Handle bsm, final Object... bsmArgs) {
    return symbolTable.addConstantInvokeDynamic(name, desc, bsm, bsmArgs).index;
  }

  /**
   * Adds a field reference to the constant pool of the class being build. Does nothing if the
   * constant pool already contains a similar item. <i>This method is intended for {@link Attribute}
   * sub classes, and is normally not needed by class generators or adapters.</i>
   *
   * @param owner the internal name of the field's owner class.
   * @param name the field's name.
   * @param desc the field's descriptor.
   * @return the index of a new or already existing field reference item.
   */
  public int newField(final String owner, final String name, final String desc) {
    return symbolTable.addConstantFieldref(owner, name, desc).index;
  }

  /**
   * Adds a method reference to the constant pool of the class being build. Does nothing if the
   * constant pool already contains a similar item. <i>This method is intended for {@link Attribute}
   * sub classes, and is normally not needed by class generators or adapters.</i>
   *
   * @param owner the internal name of the method's owner class.
   * @param name the method's name.
   * @param desc the method's descriptor.
   * @param itf <tt>true</tt> if <tt>owner</tt> is an interface.
   * @return the index of a new or already existing method reference item.
   */
  public int newMethod(
      final String owner, final String name, final String desc, final boolean itf) {
    return symbolTable.addConstantMethodref(owner, name, desc, itf).index;
  }

  /**
   * Adds a name and type to the constant pool of the class being build. Does nothing if the
   * constant pool already contains a similar item. <i>This method is intended for {@link Attribute}
   * sub classes, and is normally not needed by class generators or adapters.</i>
   *
   * @param name a name.
   * @param desc a type descriptor.
   * @return the index of a new or already existing name and type item.
   */
  public int newNameType(final String name, final String desc) {
    return symbolTable.addConstantNameAndType(name, desc);
  }

  // -----------------------------------------------------------------------------------------------
  // Default method to compute common super classes when computing stack map frames
  // -----------------------------------------------------------------------------------------------

  /**
   * Returns the common super type of the two given types. The default implementation of this method
   * <i>loads</i> the two given classes and uses the java.lang.Class methods to find the common
   * super class. It can be overridden to compute this common super type in other ways, in
   * particular without actually loading any class, or to take into account the class that is
   * currently being generated by this ClassWriter, which can of course not be loaded since it is
   * under construction.
   *
   * @param type1 the internal name of a class.
   * @param type2 the internal name of another class.
   * @return the internal name of the common super class of the two given classes.
   */
  protected String getCommonSuperClass(final String type1, final String type2) {
    Class<?> class1, class2;
    ClassLoader classLoader = getClass().getClassLoader();
    try {
      class1 = Class.forName(type1.replace('/', '.'), false, classLoader);
      class2 = Class.forName(type2.replace('/', '.'), false, classLoader);
    } catch (Exception e) {
      throw new RuntimeException(e.toString());
    }
    if (class1.isAssignableFrom(class2)) {
      return type1;
    }
    if (class2.isAssignableFrom(class1)) {
      return type2;
    }
    if (class1.isInterface() || class2.isInterface()) {
      return "java/lang/Object";
    } else {
      do {
        class1 = class1.getSuperclass();
      } while (!class1.isAssignableFrom(class2));
      return class1.getName().replace('.', '/');
    }
  }
}
