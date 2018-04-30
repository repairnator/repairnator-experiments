/*
* Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
*
* Redistribution and use in source and binary forms, with or without modification, are
* permitted provided that the following conditions are met:
*
*    1. Redistributions of source code must retain the above copyright notice, this list of
*       conditions and the following disclaimer.
*
*    2. Redistributions in binary form must reproduce the above copyright notice, this list
*       of conditions and the following disclaimer in the documentation and/or other materials
*       provided with the distribution.
*
* THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
* WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
* FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
* CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
* SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
* ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
* NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
* ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*
* The views and conclusions contained in the software and documentation are those of the
* authors and should not be interpreted as representing official policies, either expressed
* or implied, of BetaSteward_at_googlemail.com.
*/

package mage.abilities.costs.mana;

import mage.ManaSymbol;
import mage.constants.ColoredManaSymbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the mana symbols on a card.
 *
 * @author noxx
 */
public class ManaSymbols extends ArrayList<ManaSymbol> {

    private final static Map<ColoredManaSymbol, ManaSymbol> coloredManaMap = new HashMap<ColoredManaSymbol, ManaSymbol>() {{
        put(ColoredManaSymbol.W, ManaSymbol.W);
        put(ColoredManaSymbol.U, ManaSymbol.U);
        put(ColoredManaSymbol.B, ManaSymbol.B);
        put(ColoredManaSymbol.R, ManaSymbol.R);
        put(ColoredManaSymbol.G, ManaSymbol.G);
    }};

    /**
     * Contains all possible hybrid mana costs (each represents different hybrid mana symbol)
     * We'll use it for converting from hybrid mana cost to hybrid mana symbol.
     */
    private final static Map<ManaSymbol, HybridManaCost> hybridManaMap = new HashMap<>();

    /**
     *  Build map of all possible hybrid mana symbols assigning corresponding instance of hybrid mana cost.
     */
    static {
        hybridManaMap.put(ManaSymbol.HYBRID_BG, new HybridManaCost(ColoredManaSymbol.B, ColoredManaSymbol.G));
        hybridManaMap.put(ManaSymbol.HYBRID_BR, new HybridManaCost(ColoredManaSymbol.B, ColoredManaSymbol.R));
        hybridManaMap.put(ManaSymbol.HYBRID_GU, new HybridManaCost(ColoredManaSymbol.G, ColoredManaSymbol.U));
        hybridManaMap.put(ManaSymbol.HYBRID_GW, new HybridManaCost(ColoredManaSymbol.G, ColoredManaSymbol.W));
        hybridManaMap.put(ManaSymbol.HYBRID_RG, new HybridManaCost(ColoredManaSymbol.R, ColoredManaSymbol.G));
        hybridManaMap.put(ManaSymbol.HYBRID_RW, new HybridManaCost(ColoredManaSymbol.R, ColoredManaSymbol.W));
        hybridManaMap.put(ManaSymbol.HYBRID_UB, new HybridManaCost(ColoredManaSymbol.U, ColoredManaSymbol.B));
        hybridManaMap.put(ManaSymbol.HYBRID_UR, new HybridManaCost(ColoredManaSymbol.U, ColoredManaSymbol.R));
        hybridManaMap.put(ManaSymbol.HYBRID_WB, new HybridManaCost(ColoredManaSymbol.W, ColoredManaSymbol.B));
        hybridManaMap.put(ManaSymbol.HYBRID_WU, new HybridManaCost(ColoredManaSymbol.W, ColoredManaSymbol.U));
    }

    /**
     * Extracts mana symbols from {@link ManaCost} using {@link mage.ManaSymbol} as a base class for the mana symbols.
     *
     * @param manaCost
     * @return
     */
    public static ManaSymbols buildFromManaCost(ManaCost manaCost) {

        ManaSymbols manaSymbols = new ManaSymbols();

        if (manaCost instanceof ManaCostsImpl) {
            ManaCostsImpl manaCosts = (ManaCostsImpl)manaCost;
            for (int i = 0; i < manaCosts.size(); i++) {
                ManaCost mc = (ManaCost) manaCosts.get(i);
                if (mc instanceof ColoredManaCost) {
                    for (Map.Entry<ColoredManaSymbol, ManaSymbol> entry : coloredManaMap.entrySet()) {
                        if (mc.containsColor(entry.getKey())) {
                            manaSymbols.add(entry.getValue());
                            break;
                        }
                    }
                } else if (mc instanceof HybridManaCost) {
                    for (Map.Entry<ManaSymbol, HybridManaCost> entry : hybridManaMap.entrySet()) {
                        if (compareHybridCosts((HybridManaCost) mc, entry.getValue())) {
                            manaSymbols.add(entry.getKey());
                            break;
                        }
                    }
                }
            }
        }

        return manaSymbols;
    }

    /**
     * Compare two instance of hybrid mana cost.
     *
     * @param h1
     * @param h2
     *
     * @return
     */
    private static boolean compareHybridCosts(HybridManaCost h1, HybridManaCost h2) {
        return h1.getMana1() == h2.getMana1() && h1.getMana2() == h2.getMana2()
            || h1.getMana1() == h2.getMana2() && h1.getMana2() == h2.getMana1();
    }
}
