import net.runelite.mapping.Export;
import net.runelite.mapping.Implements;
import net.runelite.mapping.ObfuscatedName;

@ObfuscatedName("df")
@Implements("Floor1")
public class Floor1 {
   @ObfuscatedName("g")
   @Export("RANGES")
   static final int[] RANGES;
   @ObfuscatedName("e")
   static final float[] field1525;
   @ObfuscatedName("h")
   @Export("xList")
   static int[] xList;
   @ObfuscatedName("i")
   @Export("yList")
   static int[] yList;
   @ObfuscatedName("o")
   static boolean[] field1528;
   @ObfuscatedName("b")
   int[] field1534;
   @ObfuscatedName("z")
   @Export("multiplier")
   int multiplier;
   @ObfuscatedName("n")
   @Export("paritionClassList")
   int[] paritionClassList;
   @ObfuscatedName("l")
   @Export("classDimensions")
   int[] classDimensions;
   @ObfuscatedName("s")
   @Export("classSubClasses")
   int[] classSubClasses;
   @ObfuscatedName("y")
   @Export("classMasterbooks")
   int[] classMasterbooks;
   @ObfuscatedName("c")
   @Export("subclassBooks")
   int[][] subclassBooks;

   static {
      RANGES = new int[]{256, 128, 86, 64};
      field1525 = new float[]{1.0649863E-7F, 1.1341951E-7F, 1.2079015E-7F, 1.2863978E-7F, 1.369995E-7F, 1.459025E-7F, 1.5538409E-7F, 1.6548181E-7F, 1.7623574E-7F, 1.8768856E-7F, 1.998856E-7F, 2.128753E-7F, 2.2670913E-7F, 2.4144197E-7F, 2.5713223E-7F, 2.7384212E-7F, 2.9163792E-7F, 3.1059022E-7F, 3.307741E-7F, 3.5226967E-7F, 3.7516213E-7F, 3.995423E-7F, 4.255068E-7F, 4.5315863E-7F, 4.8260745E-7F, 5.1397E-7F, 5.4737063E-7F, 5.829419E-7F, 6.208247E-7F, 6.611694E-7F, 7.041359E-7F, 7.4989464E-7F, 7.98627E-7F, 8.505263E-7F, 9.057983E-7F, 9.646621E-7F, 1.0273513E-6F, 1.0941144E-6F, 1.1652161E-6F, 1.2409384E-6F, 1.3215816E-6F, 1.4074654E-6F, 1.4989305E-6F, 1.5963394E-6F, 1.7000785E-6F, 1.8105592E-6F, 1.9282195E-6F, 2.053526E-6F, 2.1869757E-6F, 2.3290977E-6F, 2.4804558E-6F, 2.6416496E-6F, 2.813319E-6F, 2.9961443E-6F, 3.1908505E-6F, 3.39821E-6F, 3.619045E-6F, 3.8542307E-6F, 4.1047006E-6F, 4.371447E-6F, 4.6555283E-6F, 4.958071E-6F, 5.280274E-6F, 5.623416E-6F, 5.988857E-6F, 6.3780467E-6F, 6.7925284E-6F, 7.2339453E-6F, 7.704048E-6F, 8.2047E-6F, 8.737888E-6F, 9.305725E-6F, 9.910464E-6F, 1.0554501E-5F, 1.1240392E-5F, 1.1970856E-5F, 1.2748789E-5F, 1.3577278E-5F, 1.4459606E-5F, 1.5399271E-5F, 1.6400005E-5F, 1.7465769E-5F, 1.8600793E-5F, 1.9809577E-5F, 2.1096914E-5F, 2.2467912E-5F, 2.3928002E-5F, 2.5482977E-5F, 2.7139005E-5F, 2.890265E-5F, 3.078091E-5F, 3.2781227E-5F, 3.4911533E-5F, 3.718028E-5F, 3.9596467E-5F, 4.2169668E-5F, 4.491009E-5F, 4.7828602E-5F, 5.0936775E-5F, 5.424693E-5F, 5.7772202E-5F, 6.152657E-5F, 6.552491E-5F, 6.9783084E-5F, 7.4317984E-5F, 7.914758E-5F, 8.429104E-5F, 8.976875E-5F, 9.560242E-5F, 1.0181521E-4F, 1.0843174E-4F, 1.1547824E-4F, 1.2298267E-4F, 1.3097477E-4F, 1.3948625E-4F, 1.4855085E-4F, 1.5820454E-4F, 1.6848555E-4F, 1.7943469E-4F, 1.9109536E-4F, 2.0351382E-4F, 2.167393E-4F, 2.3082423E-4F, 2.4582449E-4F, 2.6179955E-4F, 2.7881275E-4F, 2.9693157E-4F, 3.1622787E-4F, 3.3677815E-4F, 3.5866388E-4F, 3.8197188E-4F, 4.0679457E-4F, 4.3323037E-4F, 4.613841E-4F, 4.913675E-4F, 5.2329927E-4F, 5.573062E-4F, 5.935231E-4F, 6.320936E-4F, 6.731706E-4F, 7.16917E-4F, 7.635063E-4F, 8.1312325E-4F, 8.6596457E-4F, 9.2223985E-4F, 9.821722E-4F, 0.0010459992F, 0.0011139743F, 0.0011863665F, 0.0012634633F, 0.0013455702F, 0.0014330129F, 0.0015261382F, 0.0016253153F, 0.0017309374F, 0.0018434235F, 0.0019632196F, 0.0020908006F, 0.0022266726F, 0.0023713743F, 0.0025254795F, 0.0026895993F, 0.0028643848F, 0.0030505287F, 0.003248769F, 0.0034598925F, 0.0036847359F, 0.0039241905F, 0.0041792067F, 0.004450795F, 0.004740033F, 0.005048067F, 0.0053761187F, 0.005725489F, 0.0060975635F, 0.0064938175F, 0.0069158226F, 0.0073652514F, 0.007843887F, 0.008353627F, 0.008896492F, 0.009474637F, 0.010090352F, 0.01074608F, 0.011444421F, 0.012188144F, 0.012980198F, 0.013823725F, 0.014722068F, 0.015678791F, 0.016697686F, 0.017782796F, 0.018938422F, 0.020169148F, 0.021479854F, 0.022875736F, 0.02436233F, 0.025945531F, 0.027631618F, 0.029427277F, 0.031339627F, 0.03337625F, 0.035545226F, 0.037855156F, 0.0403152F, 0.042935107F, 0.045725275F, 0.048696756F, 0.05186135F, 0.05523159F, 0.05882085F, 0.062643364F, 0.06671428F, 0.07104975F, 0.075666964F, 0.08058423F, 0.08582105F, 0.09139818F, 0.097337745F, 0.1036633F, 0.11039993F, 0.11757434F, 0.12521498F, 0.13335215F, 0.14201812F, 0.15124726F, 0.16107617F, 0.1715438F, 0.18269168F, 0.19456401F, 0.20720787F, 0.22067343F, 0.23501402F, 0.25028655F, 0.26655158F, 0.28387362F, 0.3023213F, 0.32196787F, 0.34289113F, 0.36517414F, 0.3889052F, 0.41417846F, 0.44109413F, 0.4697589F, 0.50028646F, 0.53279793F, 0.5674221F, 0.6042964F, 0.64356697F, 0.6853896F, 0.72993004F, 0.777365F, 0.8278826F, 0.88168305F, 0.9389798F, 1.0F};
   }

   Floor1() {
      int var1 = class104.getInt(16);
      if(var1 != 1) {
         throw new RuntimeException();
      } else {
         int var2 = class104.getInt(5);
         int var3 = 0;
         this.paritionClassList = new int[var2];

         int var4;
         int var5;
         for(var4 = 0; var4 < var2; ++var4) {
            var5 = class104.getInt(4);
            this.paritionClassList[var4] = var5;
            if(var5 >= var3) {
               var3 = var5 + 1;
            }
         }

         this.classDimensions = new int[var3];
         this.classSubClasses = new int[var3];
         this.classMasterbooks = new int[var3];
         this.subclassBooks = new int[var3][];

         int var7;
         for(var4 = 0; var4 < var3; ++var4) {
            this.classDimensions[var4] = class104.getInt(3) + 1;
            var5 = this.classSubClasses[var4] = class104.getInt(2);
            if(var5 != 0) {
               this.classMasterbooks[var4] = class104.getInt(8);
            }

            var5 = 1 << var5;
            int[] var6 = new int[var5];
            this.subclassBooks[var4] = var6;

            for(var7 = 0; var7 < var5; ++var7) {
               var6[var7] = class104.getInt(8) - 1;
            }
         }

         this.multiplier = class104.getInt(2) + 1;
         var4 = class104.getInt(4);
         var5 = 2;

         int var9;
         for(var9 = 0; var9 < var2; ++var9) {
            var5 += this.classDimensions[this.paritionClassList[var9]];
         }

         this.field1534 = new int[var5];
         this.field1534[0] = 0;
         this.field1534[1] = 1 << var4;
         var5 = 2;

         for(var9 = 0; var9 < var2; ++var9) {
            var7 = this.paritionClassList[var9];

            for(int var8 = 0; var8 < this.classDimensions[var7]; ++var8) {
               this.field1534[var5++] = class104.getInt(var4);
            }
         }

         if(xList == null || xList.length < var5) {
            xList = new int[var5];
            yList = new int[var5];
            field1528 = new boolean[var5];
         }

      }
   }

   @ObfuscatedName("b")
   int method2275(int var1, int var2, int var3, int var4, int var5) {
      int var6 = var4 - var2;
      int var7 = var3 - var1;
      int var8 = var6 < 0?-var6:var6;
      int var9 = (var5 - var1) * var8;
      int var10 = var9 / var7;
      return var6 < 0?var2 - var10:var10 + var2;
   }

   @ObfuscatedName("z")
   void method2282(int var1, int var2, int var3, int var4, float[] var5, int var6) {
      int var7 = var4 - var2;
      int var8 = var3 - var1;
      int var9 = var7 < 0?-var7:var7;
      int var10 = var7 / var8;
      int var11 = var2;
      int var12 = 0;
      int var13 = var7 < 0?var10 - 1:var10 + 1;
      var9 -= (var10 < 0?-var10:var10) * var8;
      var5[var1] *= field1525[var2];
      if(var3 > var6) {
         var3 = var6;
      }

      for(int var14 = var1 + 1; var14 < var3; ++var14) {
         var12 += var9;
         if(var12 >= var8) {
            var12 -= var8;
            var11 += var13;
         } else {
            var11 += var10;
         }

         var5[var14] *= field1525[var11];
      }

   }

   @ObfuscatedName("n")
   void method2276(int var1, int var2) {
      if(var1 < var2) {
         int var3 = var1;
         int var4 = xList[var1];
         int var5 = yList[var1];
         boolean var6 = field1528[var1];

         for(int var7 = var1 + 1; var7 <= var2; ++var7) {
            int var8 = xList[var7];
            if(var8 < var4) {
               xList[var3] = var8;
               yList[var3] = yList[var7];
               field1528[var3] = field1528[var7];
               ++var3;
               xList[var7] = xList[var3];
               yList[var7] = yList[var3];
               field1528[var7] = field1528[var3];
            }
         }

         xList[var3] = var4;
         yList[var3] = var5;
         field1528[var3] = var6;
         this.method2276(var1, var3 - 1);
         this.method2276(var3 + 1, var2);
      }
   }

   @ObfuscatedName("l")
   @Export("decodedFloor")
   boolean decodedFloor() {
      boolean var1 = class104.getBit() != 0;
      if(!var1) {
         return false;
      } else {
         int var2 = this.field1534.length;

         int var3;
         for(var3 = 0; var3 < var2; ++var3) {
            xList[var3] = this.field1534[var3];
         }

         var3 = RANGES[this.multiplier - 1];
         int var4 = NetWriter.ilog(var3 - 1);
         yList[0] = class104.getInt(var4);
         yList[1] = class104.getInt(var4);
         int var5 = 2;

         for(int var6 = 0; var6 < this.paritionClassList.length; ++var6) {
            int var7 = this.paritionClassList[var6];
            int var8 = this.classDimensions[var7];
            int var9 = this.classSubClasses[var7];
            int var10 = (1 << var9) - 1;
            int var11 = 0;
            if(var9 > 0) {
               var11 = class104.codeBooks[this.classMasterbooks[var7]].getHuffmanRoot();
            }

            for(int var12 = 0; var12 < var8; ++var12) {
               int var13 = this.subclassBooks[var7][var11 & var10];
               var11 >>>= var9;
               yList[var5++] = var13 >= 0?class104.codeBooks[var13].getHuffmanRoot():0;
            }
         }

         return true;
      }
   }

   @ObfuscatedName("s")
   @Export("computeFloor")
   void computeFloor(float[] var1, int var2) {
      int var3 = this.field1534.length;
      int var4 = RANGES[this.multiplier - 1];
      boolean[] var5 = field1528;
      field1528[1] = true;
      var5[0] = true;

      int var6;
      int var7;
      int var8;
      int var9;
      int var10;
      for(var6 = 2; var6 < var3; ++var6) {
         var7 = method2292(xList, var6);
         var8 = method2274(xList, var6);
         var9 = this.method2275(xList[var7], yList[var7], xList[var8], yList[var8], xList[var6]);
         var10 = yList[var6];
         int var11 = var4 - var9;
         int var13 = (var11 < var9?var11:var9) << 1;
         if(var10 != 0) {
            boolean[] var14 = field1528;
            field1528[var8] = true;
            var14[var7] = true;
            field1528[var6] = true;
            if(var10 >= var13) {
               yList[var6] = var11 > var9?var9 + (var10 - var9):var11 + (var9 - var10) - 1;
            } else {
               yList[var6] = (var10 & 1) != 0?var9 - (var10 + 1) / 2:var10 / 2 + var9;
            }
         } else {
            field1528[var6] = false;
            yList[var6] = var9;
         }
      }

      this.method2276(0, var3 - 1);
      var6 = 0;
      var7 = yList[0] * this.multiplier;

      for(var8 = 1; var8 < var3; ++var8) {
         if(field1528[var8]) {
            var9 = xList[var8];
            var10 = yList[var8] * this.multiplier;
            this.method2282(var6, var7, var9, var10, var1, var2);
            if(var9 >= var2) {
               return;
            }

            var6 = var9;
            var7 = var10;
         }
      }

      float var16 = field1525[var7];

      for(var9 = var6; var9 < var2; ++var9) {
         var1[var9] *= var16;
      }

   }

   @ObfuscatedName("g")
   static int method2292(int[] var0, int var1) {
      int var2 = var0[var1];
      int var3 = -1;
      int var4 = Integer.MIN_VALUE;

      for(int var5 = 0; var5 < var1; ++var5) {
         int var6 = var0[var5];
         if(var6 < var2 && var6 > var4) {
            var3 = var5;
            var4 = var6;
         }
      }

      return var3;
   }

   @ObfuscatedName("e")
   static int method2274(int[] var0, int var1) {
      int var2 = var0[var1];
      int var3 = -1;
      int var4 = Integer.MAX_VALUE;

      for(int var5 = 0; var5 < var1; ++var5) {
         int var6 = var0[var5];
         if(var6 > var2 && var6 < var4) {
            var3 = var5;
            var4 = var6;
         }
      }

      return var3;
   }
}
