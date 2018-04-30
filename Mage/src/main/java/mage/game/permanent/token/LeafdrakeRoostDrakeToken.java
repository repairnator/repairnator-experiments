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
* THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com AS IS AND ANY EXPRESS OR IMPLIED
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
import mage.constants.CardType;
import mage.constants.SubType;
import mage.MageInt;
import mage.abilities.keyword.FlyingAbility;

/**
 *
 * @author spjspj
 */
public class LeafdrakeRoostDrakeToken extends TokenImpl {

    final static private List<String> tokenImageSets = new ArrayList<>();

    static {
        tokenImageSets.addAll(Arrays.asList("C13", "CMA"));
    }

    public LeafdrakeRoostDrakeToken() {
        this(null, 0);
    }

    public LeafdrakeRoostDrakeToken(String setCode) {
        this(setCode, 0);
    }

    public LeafdrakeRoostDrakeToken(String setCode, int tokenType) {
        super("Drake", "2/2 green and blue Drake creature token with flying");
        availableImageSetCodes = tokenImageSets;
        setOriginalExpansionSetCode(setCode);
        cardType.add(CardType.CREATURE);
        color.setGreen(true);
        color.setBlue(true);
        subtype.add(SubType.DRAKE);
        power = new MageInt(2);
        toughness = new MageInt(2);
        this.addAbility(FlyingAbility.getInstance());
    }
    
    public LeafdrakeRoostDrakeToken(final LeafdrakeRoostDrakeToken token) {
        super(token);
    }

    public LeafdrakeRoostDrakeToken copy() {
        return new LeafdrakeRoostDrakeToken(this);
    }
}
