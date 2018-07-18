package com.vip.vjtools.vjmap;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Comparator;

import sun.jvm.hotspot.oops.ArrayKlass;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Klass;
import sun.jvm.hotspot.oops.ObjArrayKlass;
import sun.jvm.hotspot.oops.TypeArrayKlass;

public class ClassStats {

	private Klass klass;
	private String description;

	public long count;
	public long size;
	public long edenCount;
	public long edenSize;
	public long survivorCount;
	public long survivorSize;
	public long oldCount;
	public long oldSize;

	public ClassStats(Klass k) {
		this.klass = k;
		this.description = initDescription();
	}

	public String initDescription() {
		Klass k = klass;
		if (k instanceof InstanceKlass)
			return k.getName().asString().replace('/', '.');

		if (k instanceof ArrayKlass) {
			ArrayKlass ak = (ArrayKlass) k;
			if (k instanceof TypeArrayKlass) {
				TypeArrayKlass tak = (TypeArrayKlass) ak;
				return tak.getElementTypeName() + "[]";
			}

			if (k instanceof ObjArrayKlass) {
				ObjArrayKlass oak = (ObjArrayKlass) ak;
				// 兼容JDK8
				// if (oak.equals(VM.getVM().getUniverse().systemObjArrayKlassObj())) {
				// return "* System ObjArray";
				// }
				Klass bottom = oak.getBottomKlass();
				StringBuilder buf = new StringBuilder(32);
				if (bottom instanceof TypeArrayKlass) {
					buf.append(((TypeArrayKlass) bottom).getElementTypeName());
				} else if (bottom instanceof InstanceKlass) {
					buf.append(bottom.getName().asString().replace('/', '.'));
				} else {
					throw new RuntimeException("should not reach here");
				}

				int dim = (int) oak.getDimension();
				for (int i = 0; i < dim; i++) {
					buf.append("[]");
				}
				return buf.toString();
			}
		}

		return getInternalName(k);
	}

	private String getInternalName(Klass k) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		klass.printValueOn(new PrintStream(bos));
		return "* " + bos.toString();
	}

	public Klass getKlass() {
		return this.klass;
	}

	public long getCount() {
		return this.count;
	}

	public long getSize() {
		return this.size;
	}

	public long getOldCount() {
		return this.oldCount;
	}

	public long getOldSize() {
		return this.oldSize;
	}

	public long getSurvivorCount() {
		return survivorCount;
	}

	public long getSurvivorSize() {
		return survivorSize;
	}

	public long getEdenCount() {
		return edenCount;
	}

	public long getEdenSize() {
		return edenSize;
	}

	public String getDescription() {
		return description;
	}

	public static Comparator<ClassStats> TOTAL_SIZE_COMPARATOR = new Comparator<ClassStats>() {
		public int compare(ClassStats o1, ClassStats o2) {
			return (int) (o2.getSize() - o1.getSize());
		}
	};

	public static Comparator<ClassStats> OLD_SIZE_COMPARATOR = new Comparator<ClassStats>() {
		public int compare(ClassStats o1, ClassStats o2) {
			return (int) (o2.getOldSize() - o1.getOldSize());
		}
	};

	public static Comparator<ClassStats> SUR_SIZE_COMPARATOR = new Comparator<ClassStats>() {
		public int compare(ClassStats o1, ClassStats o2) {
			return (int) (o2.getSurvivorSize() - o1.getSurvivorSize());
		}
	};

	public static Comparator<ClassStats> NAME_COMPARATOR = new Comparator<ClassStats>() {
		@Override
		public int compare(ClassStats o1, ClassStats o2) {
			return o1.getDescription().compareTo(o2.getDescription());
		}
	};

}