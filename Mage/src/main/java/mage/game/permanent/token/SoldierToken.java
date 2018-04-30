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
package mage.game.permanent.token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mage.MageInt;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.util.RandomUtil;

/**
 *
 * @author BetaSteward_at_googlemail.com
 */
public class SoldierToken extends TokenImpl {

    final static private List<String> tokenImageSets = new ArrayList<>();

    static {
        tokenImageSets.addAll(Arrays.asList("10E", "M15", "C14", "ORI", "ALA", "DDF", "THS", "M12", "M13", "MM2", "MMA", "RTR",
                "SOM", "DDO", "M10", "ORI", "EMN", "EMA", "CN2", "C16", "MM3", "E01"));
    }

    public SoldierToken() {
        super("Soldier", "1/1 white Soldier creature token");
        availableImageSetCodes = tokenImageSets;

        cardType.add(CardType.CREATURE);
        color.setWhite(true);
        subtype.add(SubType.SOLDIER);
        power = new MageInt(1);
        toughness = new MageInt(1);

    }

    @Override
    public void setExpansionSetCodeForImage(String code) {
        super.setExpansionSetCodeForImage(code);
        if (getOriginalExpansionSetCode() != null && getOriginalExpansionSetCode().equals("THS")) {
            this.setTokenType(RandomUtil.nextInt(2) + 1);
        }
        if (getOriginalExpansionSetCode() != null && getOriginalExpansionSetCode().equals("CN2") || getOriginalExpansionSetCode().equals("MM3")) {
            setTokenType(1);
        }
    }

    public SoldierToken(final SoldierToken token) {
        super(token);
    }

    @Override
    public SoldierToken copy() {
        return new SoldierToken(this); //To change body of generated methods, choose Tools | Templates.
    }
}
