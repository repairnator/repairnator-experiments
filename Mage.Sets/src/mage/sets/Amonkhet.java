/*
 * Copyright 2011 BetaSteward_at_googlemail.com. All rights reserved.
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
package mage.sets;

import mage.cards.ExpansionSet;
import mage.cards.repository.CardCriteria;
import mage.cards.repository.CardInfo;
import mage.cards.repository.CardRepository;
import mage.constants.Rarity;
import mage.constants.SetType;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fireshoes
 */
public class Amonkhet extends ExpansionSet {

    private static final Amonkhet instance = new Amonkhet();

    public static Amonkhet getInstance() {
        return instance;
    }

    protected final List<CardInfo> savedSpecialLand = new ArrayList<>();

    private Amonkhet() {
        super("Amonkhet", "AKH", ExpansionSet.buildDate(2017, 4, 28), SetType.EXPANSION);
        this.blockName = "Amonkhet";
        this.hasBoosters = true;
        this.hasBasicLands = true;
        this.numBoosterLands = 1;
        this.numBoosterCommon = 10;
        this.numBoosterUncommon = 3;
        this.numBoosterRare = 1;
        this.ratioBoosterMythic = 8;
        this.maxCardNumberInBooster = 269;
        this.ratioBoosterSpecialLand = 144;

        cards.add(new SetCardInfo("Ahn-Crop Champion", 194, Rarity.UNCOMMON, mage.cards.a.AhnCropChampion.class));
        cards.add(new SetCardInfo("Ahn-Crop Crasher", 117, Rarity.UNCOMMON, mage.cards.a.AhnCropCrasher.class));
        cards.add(new SetCardInfo("Ancient Crab", 40, Rarity.COMMON, mage.cards.a.AncientCrab.class));
        cards.add(new SetCardInfo("Angel of Sanctions", 1, Rarity.MYTHIC, mage.cards.a.AngelOfSanctions.class));
        cards.add(new SetCardInfo("Angler Drake", 41, Rarity.UNCOMMON, mage.cards.a.AnglerDrake.class));
        cards.add(new SetCardInfo("Anointed Procession", 2, Rarity.RARE, mage.cards.a.AnointedProcession.class));
        cards.add(new SetCardInfo("Anointer Priest", 3, Rarity.COMMON, mage.cards.a.AnointerPriest.class));
        cards.add(new SetCardInfo("Approach of the Second Sun", 4, Rarity.RARE, mage.cards.a.ApproachOfTheSecondSun.class));
        cards.add(new SetCardInfo("Archfiend of Ifnir", 78, Rarity.RARE, mage.cards.a.ArchfiendOfIfnir.class));
        cards.add(new SetCardInfo("As Foretold", 42, Rarity.MYTHIC, mage.cards.a.AsForetold.class));
        cards.add(new SetCardInfo("Aven Initiate", 43, Rarity.COMMON, mage.cards.a.AvenInitiate.class));
        cards.add(new SetCardInfo("Aven Mindcensor", 5, Rarity.RARE, mage.cards.a.AvenMindcensor.class));
        cards.add(new SetCardInfo("Aven Wind Guide", 195, Rarity.UNCOMMON, mage.cards.a.AvenWindGuide.class));
        cards.add(new SetCardInfo("Baleful Ammit", 79, Rarity.UNCOMMON, mage.cards.b.BalefulAmmit.class));
        cards.add(new SetCardInfo("Battlefield Scavenger", 118, Rarity.UNCOMMON, mage.cards.b.BattlefieldScavenger.class));
        cards.add(new SetCardInfo("Benefaction of Rhonas", 156, Rarity.COMMON, mage.cards.b.BenefactionOfRhonas.class));
        cards.add(new SetCardInfo("Binding Mummy", 6, Rarity.COMMON, mage.cards.b.BindingMummy.class));
        cards.add(new SetCardInfo("Bitterblade Warrior", 157, Rarity.COMMON, mage.cards.b.BitterbladeWarrior.class));
        cards.add(new SetCardInfo("Blazing Volley", 119, Rarity.COMMON, mage.cards.b.BlazingVolley.class));
        cards.add(new SetCardInfo("Blighted Bat", 80, Rarity.COMMON, mage.cards.b.BlightedBat.class));
        cards.add(new SetCardInfo("Bloodlust Inciter", 120, Rarity.COMMON, mage.cards.b.BloodlustInciter.class));
        cards.add(new SetCardInfo("Bloodrage Brawler", 121, Rarity.UNCOMMON, mage.cards.b.BloodrageBrawler.class));
        cards.add(new SetCardInfo("Bone Picker", 81, Rarity.UNCOMMON, mage.cards.b.BonePicker.class));
        cards.add(new SetCardInfo("Bounty of the Luxa", 196, Rarity.RARE, mage.cards.b.BountyOfTheLuxa.class));
        cards.add(new SetCardInfo("Bontu's Monument", 225, Rarity.UNCOMMON, mage.cards.b.BontusMonument.class));
        cards.add(new SetCardInfo("Bontu the Glorified", 82, Rarity.MYTHIC, mage.cards.b.BontuTheGlorified.class));
        cards.add(new SetCardInfo("Brute Strength", 122, Rarity.COMMON, mage.cards.b.BruteStrength.class));
        cards.add(new SetCardInfo("By Force", 123, Rarity.UNCOMMON, mage.cards.b.ByForce.class));
        cards.add(new SetCardInfo("Cancel", 44, Rarity.COMMON, mage.cards.c.Cancel.class));
        cards.add(new SetCardInfo("Canyon Slough", 239, Rarity.RARE, mage.cards.c.CanyonSlough.class));
        cards.add(new SetCardInfo("Cartouche of Ambition", 83, Rarity.COMMON, mage.cards.c.CartoucheOfAmbition.class));
        cards.add(new SetCardInfo("Cartouche of Knowledge", 45, Rarity.COMMON, mage.cards.c.CartoucheOfKnowledge.class));
        cards.add(new SetCardInfo("Cartouche of Solidarity", 7, Rarity.COMMON, mage.cards.c.CartoucheOfSolidarity.class));
        cards.add(new SetCardInfo("Cartouche of Strength", 158, Rarity.COMMON, mage.cards.c.CartoucheOfStrength.class));
        cards.add(new SetCardInfo("Cartouche of Zeal", 124, Rarity.COMMON, mage.cards.c.CartoucheOfZeal.class));
        cards.add(new SetCardInfo("Cascading Cataracts", 240, Rarity.RARE, mage.cards.c.CascadingCataracts.class));
        cards.add(new SetCardInfo("Cast Out", 8, Rarity.UNCOMMON, mage.cards.c.CastOut.class));
        cards.add(new SetCardInfo("Censor", 46, Rarity.UNCOMMON, mage.cards.c.Censor.class));
        cards.add(new SetCardInfo("Champion of Rhonas", 159, Rarity.RARE, mage.cards.c.ChampionOfRhonas.class));
        cards.add(new SetCardInfo("Channeler Initiate", 160, Rarity.RARE, mage.cards.c.ChannelerInitiate.class));
        cards.add(new SetCardInfo("Cinder Barrens", 280, Rarity.COMMON, mage.cards.c.CinderBarrens.class));
        cards.add(new SetCardInfo("Colossapede", 161, Rarity.COMMON, mage.cards.c.Colossapede.class));
        cards.add(new SetCardInfo("Combat Celebrant", 125, Rarity.MYTHIC, mage.cards.c.CombatCelebrant.class));
        cards.add(new SetCardInfo("Commit // Memory", 211, Rarity.RARE, mage.cards.c.CommitMemory.class));
        cards.add(new SetCardInfo("Companion of the Trials", 271, Rarity.UNCOMMON, mage.cards.c.CompanionOfTheTrials.class));
        cards.add(new SetCardInfo("Compelling Argument", 47, Rarity.COMMON, mage.cards.c.CompellingArgument.class));
        cards.add(new SetCardInfo("Compulsory Rest", 9, Rarity.COMMON, mage.cards.c.CompulsoryRest.class));
        cards.add(new SetCardInfo("Consuming Fervor", 126, Rarity.UNCOMMON, mage.cards.c.ConsumingFervor.class));
        cards.add(new SetCardInfo("Cradle of the Accursed", 241, Rarity.COMMON, mage.cards.c.CradleOfTheAccursed.class));
        cards.add(new SetCardInfo("Crocodile of the Crossing", 162, Rarity.UNCOMMON, mage.cards.c.CrocodileOfTheCrossing.class));
        cards.add(new SetCardInfo("Cryptic Serpent", 48, Rarity.UNCOMMON, mage.cards.c.CrypticSerpent.class));
        cards.add(new SetCardInfo("Cruel Reality", 84, Rarity.MYTHIC, mage.cards.c.CruelReality.class));
        cards.add(new SetCardInfo("Curator of Mysteries", 49, Rarity.RARE, mage.cards.c.CuratorOfMysteries.class));
        cards.add(new SetCardInfo("Cursed Minotaur", 85, Rarity.COMMON, mage.cards.c.CursedMinotaur.class));
        cards.add(new SetCardInfo("Cut // Ribbons", 223, Rarity.RARE, mage.cards.c.CutRibbons.class));
        cards.add(new SetCardInfo("Decimator Beetle", 197, Rarity.UNCOMMON, mage.cards.d.DecimatorBeetle.class));
        cards.add(new SetCardInfo("Decision Paralysis", 50, Rarity.COMMON, mage.cards.d.DecisionParalysis.class));
        cards.add(new SetCardInfo("Deem Worthy", 127, Rarity.UNCOMMON, mage.cards.d.DeemWorthy.class));
        cards.add(new SetCardInfo("Defiant Greatmaw", 163, Rarity.UNCOMMON, mage.cards.d.DefiantGreatmaw.class));
        cards.add(new SetCardInfo("Desert Cerodon", 128, Rarity.COMMON, mage.cards.d.DesertCerodon.class));
        cards.add(new SetCardInfo("Desiccated Naga", 276, Rarity.UNCOMMON, mage.cards.d.DesiccatedNaga.class));
        cards.add(new SetCardInfo("Destined // Lead", 217, Rarity.UNCOMMON, mage.cards.d.DestinedLead.class));
        cards.add(new SetCardInfo("Devoted Crop-Mate", 10, Rarity.UNCOMMON, mage.cards.d.DevotedCropMate.class));
        cards.add(new SetCardInfo("Dispossess", 86, Rarity.RARE, mage.cards.d.Dispossess.class));
        cards.add(new SetCardInfo("Dissenter's Deliverance", 164, Rarity.COMMON, mage.cards.d.DissentersDeliverance.class));
        cards.add(new SetCardInfo("Djeru's Resolve", 11, Rarity.COMMON, mage.cards.d.DjerusResolve.class));
        cards.add(new SetCardInfo("Doomed Dissenter", 87, Rarity.COMMON, mage.cards.d.DoomedDissenter.class));
        cards.add(new SetCardInfo("Drake Haven", 51, Rarity.RARE, mage.cards.d.DrakeHaven.class));
        cards.add(new SetCardInfo("Dread Wanderer", 88, Rarity.RARE, mage.cards.d.DreadWanderer.class));
        cards.add(new SetCardInfo("Dune Beetle", 89, Rarity.COMMON, mage.cards.d.DuneBeetle.class));
        cards.add(new SetCardInfo("Dusk // Dawn", 210, Rarity.RARE, mage.cards.d.DuskDawn.class));
        cards.add(new SetCardInfo("Edifice of Authority", 226, Rarity.UNCOMMON, mage.cards.e.EdificeOfAuthority.class));
        cards.add(new SetCardInfo("Electrify", 129, Rarity.COMMON, mage.cards.e.Electrify.class));
        cards.add(new SetCardInfo("Embalmer's Tools", 227, Rarity.UNCOMMON, mage.cards.e.EmbalmersTools.class));
        cards.add(new SetCardInfo("Emberhorn Minotaur", 130, Rarity.COMMON, mage.cards.e.EmberhornMinotaur.class));
        cards.add(new SetCardInfo("Enigma Drake", 198, Rarity.UNCOMMON, mage.cards.e.EnigmaDrake.class));
        cards.add(new SetCardInfo("Essence Scatter", 52, Rarity.COMMON, mage.cards.e.EssenceScatter.class));
        cards.add(new SetCardInfo("Evolving Wilds", 242, Rarity.COMMON, mage.cards.e.EvolvingWilds.class));
        cards.add(new SetCardInfo("Exemplar of Strength", 165, Rarity.UNCOMMON, mage.cards.e.ExemplarOfStrength.class));
        cards.add(new SetCardInfo("Failure // Comply", 221, Rarity.RARE, mage.cards.f.FailureComply.class));
        cards.add(new SetCardInfo("Faith of the Devoted", 90, Rarity.UNCOMMON, mage.cards.f.FaithOfTheDevoted.class));
        cards.add(new SetCardInfo("Fan Bearer", 12, Rarity.COMMON, mage.cards.f.FanBearer.class));
        cards.add(new SetCardInfo("Festering Mummy", 91, Rarity.COMMON, mage.cards.f.FesteringMummy.class));
        cards.add(new SetCardInfo("Fetid Pools", 243, Rarity.RARE, mage.cards.f.FetidPools.class));
        cards.add(new SetCardInfo("Final Reward", 92, Rarity.COMMON, mage.cards.f.FinalReward.class));
        cards.add(new SetCardInfo("Flameblade Adept", 131, Rarity.UNCOMMON, mage.cards.f.FlamebladeAdept.class));
        cards.add(new SetCardInfo("Fling", 132, Rarity.COMMON, mage.cards.f.Fling.class));
        cards.add(new SetCardInfo("Floodwaters", 53, Rarity.COMMON, mage.cards.f.Floodwaters.class));
        cards.add(new SetCardInfo("Forest", 254, Rarity.LAND, mage.cards.basiclands.Forest.class, FULL_ART_BFZ_VARIOUS));
        cards.add(new SetCardInfo("Forest", 267, Rarity.LAND, mage.cards.basiclands.Forest.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Forest", 268, Rarity.LAND, mage.cards.basiclands.Forest.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Forest", 269, Rarity.LAND, mage.cards.basiclands.Forest.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Forsake the Worldly", 13, Rarity.COMMON, mage.cards.f.ForsakeTheWorldly.class));
        cards.add(new SetCardInfo("Forsaken Sanctuary", 281, Rarity.COMMON, mage.cards.f.ForsakenSanctuary.class));
        cards.add(new SetCardInfo("Foul Orchard", 279, Rarity.COMMON, mage.cards.f.FoulOrchard.class));
        cards.add(new SetCardInfo("Galestrike", 54, Rarity.UNCOMMON, mage.cards.g.Galestrike.class));
        cards.add(new SetCardInfo("Gate to the Afterlife", 228, Rarity.UNCOMMON, mage.cards.g.GateToTheAfterlife.class));
        cards.add(new SetCardInfo("Giant Spider", 166, Rarity.COMMON, mage.cards.g.GiantSpider.class));
        cards.add(new SetCardInfo("Gideon of the Trials", 14, Rarity.MYTHIC, mage.cards.g.GideonOfTheTrials.class));
        cards.add(new SetCardInfo("Gideon's Intervention", 15, Rarity.RARE, mage.cards.g.GideonsIntervention.class));
        cards.add(new SetCardInfo("Gideon's Resolve", 272, Rarity.RARE, mage.cards.g.GideonsResolve.class));
        cards.add(new SetCardInfo("Gideon, Martial Paragon", 270, Rarity.MYTHIC, mage.cards.g.GideonMartialParagon.class));
        cards.add(new SetCardInfo("Gift of Paradise", 167, Rarity.COMMON, mage.cards.g.GiftOfParadise.class));
        cards.add(new SetCardInfo("Glorious End", 133, Rarity.MYTHIC, mage.cards.g.GloriousEnd.class));
        cards.add(new SetCardInfo("Glory-Bound Initiate", 16, Rarity.RARE, mage.cards.g.GloryBoundInitiate.class));
        cards.add(new SetCardInfo("Glorybringer", 134, Rarity.RARE, mage.cards.g.Glorybringer.class));
        cards.add(new SetCardInfo("Glyph Keeper", 55, Rarity.RARE, mage.cards.g.GlyphKeeper.class));
        cards.add(new SetCardInfo("Graceful Cat", 273, Rarity.COMMON, mage.cards.g.GracefulCat.class));
        cards.add(new SetCardInfo("Grasping Dunes", 244, Rarity.UNCOMMON, mage.cards.g.GraspingDunes.class));
        cards.add(new SetCardInfo("Gravedigger", 93, Rarity.UNCOMMON, mage.cards.g.Gravedigger.class));
        cards.add(new SetCardInfo("Greater Sandwurm", 168, Rarity.COMMON, mage.cards.g.GreaterSandwurm.class));
        cards.add(new SetCardInfo("Grim Strider", 94, Rarity.UNCOMMON, mage.cards.g.GrimStrider.class));
        cards.add(new SetCardInfo("Gust Walker", 17, Rarity.COMMON, mage.cards.g.GustWalker.class));
        cards.add(new SetCardInfo("Hapatra's Mark", 169, Rarity.UNCOMMON, mage.cards.h.HapatrasMark.class));
        cards.add(new SetCardInfo("Hapatra, Vizier of Poisons", 199, Rarity.RARE, mage.cards.h.HapatraVizierOfPoisons.class));
        cards.add(new SetCardInfo("Harsh Mentor", 135, Rarity.RARE, mage.cards.h.HarshMentor.class));
        cards.add(new SetCardInfo("Harvest Season", 170, Rarity.RARE, mage.cards.h.HarvestSeason.class));
        cards.add(new SetCardInfo("Haze of Pollen", 171, Rarity.COMMON, mage.cards.h.HazeOfPollen.class));
        cards.add(new SetCardInfo("Hazoret the Fervent", 136, Rarity.MYTHIC, mage.cards.h.HazoretTheFervent.class));
        cards.add(new SetCardInfo("Hazoret's Favor", 137, Rarity.RARE, mage.cards.h.HazoretsFavor.class));
        cards.add(new SetCardInfo("Hazoret's Monument", 229, Rarity.UNCOMMON, mage.cards.h.HazoretsMonument.class));
        cards.add(new SetCardInfo("Heart-Piercer Manticore", 138, Rarity.RARE, mage.cards.h.HeartPiercerManticore.class));
        cards.add(new SetCardInfo("Heaven // Earth", 224, Rarity.RARE, mage.cards.h.HeavenEarth.class));
        cards.add(new SetCardInfo("Hekma Sentinels", 56, Rarity.COMMON, mage.cards.h.HekmaSentinels.class));
        cards.add(new SetCardInfo("Hieroglyphic Illumination", 57, Rarity.COMMON, mage.cards.h.HieroglyphicIllumination.class));
        cards.add(new SetCardInfo("Highland Lake", 282, Rarity.COMMON, mage.cards.h.HighlandLake.class));
        cards.add(new SetCardInfo("Honed Khopesh", 230, Rarity.COMMON, mage.cards.h.HonedKhopesh.class));
        cards.add(new SetCardInfo("Honored Crop-Captain", 200, Rarity.UNCOMMON, mage.cards.h.HonoredCropCaptain.class));
        cards.add(new SetCardInfo("Honored Hydra", 172, Rarity.RARE, mage.cards.h.HonoredHydra.class));
        cards.add(new SetCardInfo("Hooded Brawler", 173, Rarity.COMMON, mage.cards.h.HoodedBrawler.class));
        cards.add(new SetCardInfo("Horror of the Broken Lands", 95, Rarity.COMMON, mage.cards.h.HorrorOfTheBrokenLands.class));
        cards.add(new SetCardInfo("Hyena Pack", 139, Rarity.COMMON, mage.cards.h.HyenaPack.class));
        cards.add(new SetCardInfo("Illusory Wrappings", 58, Rarity.COMMON, mage.cards.i.IllusoryWrappings.class));
        cards.add(new SetCardInfo("Impeccable Timing", 18, Rarity.COMMON, mage.cards.i.ImpeccableTiming.class));
        cards.add(new SetCardInfo("In Oketra's Name", 19, Rarity.COMMON, mage.cards.i.InOketrasName.class));
        cards.add(new SetCardInfo("Initiate's Companion", 174, Rarity.COMMON, mage.cards.i.InitiatesCompanion.class));
        cards.add(new SetCardInfo("Insult // Injury", 213, Rarity.RARE, mage.cards.i.InsultInjury.class));
        cards.add(new SetCardInfo("Irrigated Farmland", 245, Rarity.RARE, mage.cards.i.IrrigatedFarmland.class));
        cards.add(new SetCardInfo("Island", 251, Rarity.LAND, mage.cards.basiclands.Island.class, FULL_ART_BFZ_VARIOUS));
        cards.add(new SetCardInfo("Island", 258, Rarity.LAND, mage.cards.basiclands.Island.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Island", 259, Rarity.LAND, mage.cards.basiclands.Island.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Island", 260, Rarity.LAND, mage.cards.basiclands.Island.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Kefnet the Mindful", 59, Rarity.MYTHIC, mage.cards.k.KefnetTheMindful.class));
        cards.add(new SetCardInfo("Kefnet's Monument", 231, Rarity.UNCOMMON, mage.cards.k.KefnetsMonument.class));
        cards.add(new SetCardInfo("Khenra Charioteer", 201, Rarity.UNCOMMON, mage.cards.k.KhenraCharioteer.class));
        cards.add(new SetCardInfo("Labyrinth Guardian", 60, Rarity.UNCOMMON, mage.cards.l.LabyrinthGuardian.class));
        cards.add(new SetCardInfo("Lay Bare the Heart", 96, Rarity.UNCOMMON, mage.cards.l.LayBareTheHeart.class));
        cards.add(new SetCardInfo("Lay Claim", 61, Rarity.UNCOMMON, mage.cards.l.LayClaim.class));
        cards.add(new SetCardInfo("Liliana's Influence", 277, Rarity.RARE, mage.cards.l.LilianasInfluence.class));
        cards.add(new SetCardInfo("Liliana's Mastery", 98, Rarity.RARE, mage.cards.l.LilianasMastery.class));
        cards.add(new SetCardInfo("Liliana, Death Wielder", 275, Rarity.MYTHIC, mage.cards.l.LilianaDeathWielder.class));
        cards.add(new SetCardInfo("Liliana, Death's Majesty", 97, Rarity.MYTHIC, mage.cards.l.LilianaDeathsMajesty.class));
        cards.add(new SetCardInfo("Limits of Solidarity", 140, Rarity.UNCOMMON, mage.cards.l.LimitsOfSolidarity.class));
        cards.add(new SetCardInfo("Lord of the Accursed", 99, Rarity.UNCOMMON, mage.cards.l.LordOfTheAccursed.class));
        cards.add(new SetCardInfo("Luxa River Shrine", 232, Rarity.COMMON, mage.cards.l.LuxaRiverShrine.class));
        cards.add(new SetCardInfo("Magma Spray", 141, Rarity.COMMON, mage.cards.m.MagmaSpray.class));
        cards.add(new SetCardInfo("Manglehorn", 175, Rarity.UNCOMMON, mage.cards.m.Manglehorn.class));
        cards.add(new SetCardInfo("Manticore of the Gauntlet", 142, Rarity.COMMON, mage.cards.m.ManticoreOfTheGauntlet.class));
        cards.add(new SetCardInfo("Meandering River", 283, Rarity.COMMON, mage.cards.m.MeanderingRiver.class));
        cards.add(new SetCardInfo("Merciless Javelineer", 202, Rarity.UNCOMMON, mage.cards.m.MercilessJavelineer.class));
        cards.add(new SetCardInfo("Miasmic Mummy", 100, Rarity.COMMON, mage.cards.m.MiasmicMummy.class));
        cards.add(new SetCardInfo("Mighty Leap", 20, Rarity.COMMON, mage.cards.m.MightyLeap.class));
        cards.add(new SetCardInfo("Minotaur Sureshot", 143, Rarity.COMMON, mage.cards.m.MinotaurSureshot.class));
        cards.add(new SetCardInfo("Mountain", 253, Rarity.LAND, mage.cards.basiclands.Mountain.class, FULL_ART_BFZ_VARIOUS));
        cards.add(new SetCardInfo("Mountain", 264, Rarity.LAND, mage.cards.basiclands.Mountain.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Mountain", 265, Rarity.LAND, mage.cards.basiclands.Mountain.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Mountain", 266, Rarity.LAND, mage.cards.basiclands.Mountain.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Mouth // Feed", 214, Rarity.RARE, mage.cards.m.MouthFeed.class));
        cards.add(new SetCardInfo("Naga Oracle", 62, Rarity.COMMON, mage.cards.n.NagaOracle.class));
        cards.add(new SetCardInfo("Naga Vitalist", 176, Rarity.COMMON, mage.cards.n.NagaVitalist.class));
        cards.add(new SetCardInfo("Nef-Crop Entangler", 144, Rarity.COMMON, mage.cards.n.NefCropEntangler.class));
        cards.add(new SetCardInfo("Neheb, the Worthy", 203, Rarity.RARE, mage.cards.n.NehebTheWorthy.class));
        cards.add(new SetCardInfo("Nest of Scarabs", 101, Rarity.UNCOMMON, mage.cards.n.NestOfScarabs.class));
        cards.add(new SetCardInfo("Never // Return", 212, Rarity.RARE, mage.cards.n.NeverReturn.class));
        cards.add(new SetCardInfo("New Perspectives", 63, Rarity.RARE, mage.cards.n.NewPerspectives.class));
        cards.add(new SetCardInfo("Nimble-Blade Khenra", 145, Rarity.COMMON, mage.cards.n.NimbleBladeKhenra.class));
        cards.add(new SetCardInfo("Nissa, Steward of Elements", 204, Rarity.MYTHIC, mage.cards.n.NissaStewardOfElements.class));
        cards.add(new SetCardInfo("Oashra Cultivator", 177, Rarity.COMMON, mage.cards.o.OashraCultivator.class));
        cards.add(new SetCardInfo("Oketra the True", 21, Rarity.MYTHIC, mage.cards.o.OketraTheTrue.class));
        cards.add(new SetCardInfo("Oketra's Attendant", 22, Rarity.UNCOMMON, mage.cards.o.OketrasAttendant.class));
        cards.add(new SetCardInfo("Oketra's Monument", 233, Rarity.UNCOMMON, mage.cards.o.OketrasMonument.class));
        cards.add(new SetCardInfo("Onward // Victory", 218, Rarity.UNCOMMON, mage.cards.o.OnwardVictory.class));
        cards.add(new SetCardInfo("Open into Wonder", 64, Rarity.UNCOMMON, mage.cards.o.OpenIntoWonder.class));
        cards.add(new SetCardInfo("Oracle's Vault", 234, Rarity.RARE, mage.cards.o.OraclesVault.class));
        cards.add(new SetCardInfo("Ornery Kudu", 178, Rarity.COMMON, mage.cards.o.OrneryKudu.class));
        cards.add(new SetCardInfo("Painful Lesson", 102, Rarity.COMMON, mage.cards.p.PainfulLesson.class));
        cards.add(new SetCardInfo("Painted Bluffs", 246, Rarity.COMMON, mage.cards.p.PaintedBluffs.class));
        cards.add(new SetCardInfo("Pathmaker Initiate", 146, Rarity.COMMON, mage.cards.p.PathmakerInitiate.class));
        cards.add(new SetCardInfo("Pitiless Vizier", 103, Rarity.COMMON, mage.cards.p.PitilessVizier.class));
        cards.add(new SetCardInfo("Plague Belcher", 104, Rarity.RARE, mage.cards.p.PlagueBelcher.class));
        cards.add(new SetCardInfo("Pyramid of the Pantheon", 235, Rarity.RARE, mage.cards.p.PyramidOfThePantheon.class));
        cards.add(new SetCardInfo("Plains", 250, Rarity.LAND, mage.cards.basiclands.Plains.class, FULL_ART_BFZ_VARIOUS));
        cards.add(new SetCardInfo("Plains", 255, Rarity.LAND, mage.cards.basiclands.Plains.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Plains", 256, Rarity.LAND, mage.cards.basiclands.Plains.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Plains", 257, Rarity.LAND, mage.cards.basiclands.Plains.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Pouncing Cheetah", 179, Rarity.COMMON, mage.cards.p.PouncingCheetah.class));
        cards.add(new SetCardInfo("Prepare // Fight", 220, Rarity.RARE, mage.cards.p.PrepareFight.class));
        cards.add(new SetCardInfo("Protection of the Hekma", 23, Rarity.UNCOMMON, mage.cards.p.ProtectionOfTheHekma.class));
        cards.add(new SetCardInfo("Prowling Serpopard", 180, Rarity.RARE, mage.cards.p.ProwlingSerpopard.class));
        cards.add(new SetCardInfo("Pull from Tomorrow", 65, Rarity.RARE, mage.cards.p.PullFromTomorrow.class));
        cards.add(new SetCardInfo("Pursue Glory", 147, Rarity.COMMON, mage.cards.p.PursueGlory.class));
        cards.add(new SetCardInfo("Quarry Hauler", 181, Rarity.COMMON, mage.cards.q.QuarryHauler.class));
        cards.add(new SetCardInfo("Rags // Riches", 222, Rarity.RARE, mage.cards.r.RagsRiches.class));
        cards.add(new SetCardInfo("Reduce // Rubble", 216, Rarity.UNCOMMON, mage.cards.r.ReduceRubble.class));
        cards.add(new SetCardInfo("Regal Caracal", 24, Rarity.RARE, mage.cards.r.RegalCaracal.class));
        cards.add(new SetCardInfo("Renewed Faith", 25, Rarity.UNCOMMON, mage.cards.r.RenewedFaith.class));
        cards.add(new SetCardInfo("Rhet-Crop Spearmaster", 26, Rarity.COMMON, mage.cards.r.RhetCropSpearmaster.class));
        cards.add(new SetCardInfo("Rhonas's Monument", 236, Rarity.UNCOMMON, mage.cards.r.RhonassMonument.class));
        cards.add(new SetCardInfo("Rhonas the Indomitable", 182, Rarity.MYTHIC, mage.cards.r.RhonasTheIndomitable.class));
        cards.add(new SetCardInfo("River Serpent", 66, Rarity.COMMON, mage.cards.r.RiverSerpent.class));
        cards.add(new SetCardInfo("Ruthless Sniper", 105, Rarity.UNCOMMON, mage.cards.r.RuthlessSniper.class));
        cards.add(new SetCardInfo("Sacred Cat", 27, Rarity.COMMON, mage.cards.s.SacredCat.class));
        cards.add(new SetCardInfo("Sacred Excavation", 67, Rarity.UNCOMMON, mage.cards.s.SacredExcavation.class));
        cards.add(new SetCardInfo("Samut, Voice of Dissent", 205, Rarity.MYTHIC, mage.cards.s.SamutVoiceOfDissent.class));
        cards.add(new SetCardInfo("Sandwurm Convergence", 183, Rarity.RARE, mage.cards.s.SandwurmConvergence.class));
        cards.add(new SetCardInfo("Scaled Behemoth", 184, Rarity.UNCOMMON, mage.cards.s.ScaledBehemoth.class));
        cards.add(new SetCardInfo("Scarab Feast", 106, Rarity.COMMON, mage.cards.s.ScarabFeast.class));
        cards.add(new SetCardInfo("Scattered Groves", 247, Rarity.RARE, mage.cards.s.ScatteredGroves.class));
        cards.add(new SetCardInfo("Scribe of the Mindful", 68, Rarity.COMMON, mage.cards.s.ScribeOfTheMindful.class));
        cards.add(new SetCardInfo("Seeker of Insight", 69, Rarity.COMMON, mage.cards.s.SeekerOfInsight.class));
        cards.add(new SetCardInfo("Seraph of the Suns", 28, Rarity.UNCOMMON, mage.cards.s.SeraphOfTheSuns.class));
        cards.add(new SetCardInfo("Shadow of the Grave", 107, Rarity.RARE, mage.cards.s.ShadowOfTheGrave.class));
        cards.add(new SetCardInfo("Shadowstorm Vizier", 206, Rarity.UNCOMMON, mage.cards.s.ShadowstormVizier.class));
        cards.add(new SetCardInfo("Shed Weakness", 185, Rarity.COMMON, mage.cards.s.ShedWeakness.class));
        cards.add(new SetCardInfo("Shefet Monitor", 186, Rarity.UNCOMMON, mage.cards.s.ShefetMonitor.class));
        cards.add(new SetCardInfo("Sheltered Thicket", 248, Rarity.RARE, mage.cards.s.ShelteredThicket.class));
        cards.add(new SetCardInfo("Shimmerscale Drake", 70, Rarity.COMMON, mage.cards.s.ShimmerscaleDrake.class));
        cards.add(new SetCardInfo("Sixth Sense", 187, Rarity.UNCOMMON, mage.cards.s.SixthSense.class));
        cards.add(new SetCardInfo("Slither Blade", 71, Rarity.COMMON, mage.cards.s.SlitherBlade.class));
        cards.add(new SetCardInfo("Soul-Scar Mage", 148, Rarity.RARE, mage.cards.s.SoulScarMage.class));
        cards.add(new SetCardInfo("Soulstinger", 108, Rarity.COMMON, mage.cards.s.Soulstinger.class));
        cards.add(new SetCardInfo("Sparring Mummy", 29, Rarity.COMMON, mage.cards.s.SparringMummy.class));
        cards.add(new SetCardInfo("Spidery Grasp", 188, Rarity.COMMON, mage.cards.s.SpideryGrasp.class));
        cards.add(new SetCardInfo("Splendid Agony", 109, Rarity.COMMON, mage.cards.s.SplendidAgony.class));
        cards.add(new SetCardInfo("Spring // Mind", 219, Rarity.UNCOMMON, mage.cards.s.SpringMind.class));
        cards.add(new SetCardInfo("Start // Finish", 215, Rarity.UNCOMMON, mage.cards.s.StartFinish.class));
        cards.add(new SetCardInfo("Stinging Shot", 189, Rarity.COMMON, mage.cards.s.StingingShot.class));
        cards.add(new SetCardInfo("Stir the Sands", 110, Rarity.UNCOMMON, mage.cards.s.StirTheSands.class));
        cards.add(new SetCardInfo("Stone Quarry", 274, Rarity.COMMON, mage.cards.s.StoneQuarry.class));
        cards.add(new SetCardInfo("Submerged Boneyard", 284, Rarity.COMMON, mage.cards.s.SubmergedBoneyard.class));
        cards.add(new SetCardInfo("Sunscorched Desert", 249, Rarity.COMMON, mage.cards.s.SunscorchedDesert.class));
        cards.add(new SetCardInfo("Supernatural Stamina", 111, Rarity.COMMON, mage.cards.s.SupernaturalStamina.class));
        cards.add(new SetCardInfo("Supply Caravan", 30, Rarity.COMMON, mage.cards.s.SupplyCaravan.class));
        cards.add(new SetCardInfo("Swamp", 252, Rarity.LAND, mage.cards.basiclands.Swamp.class, FULL_ART_BFZ_VARIOUS));
        cards.add(new SetCardInfo("Swamp", 261, Rarity.LAND, mage.cards.basiclands.Swamp.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Swamp", 262, Rarity.LAND, mage.cards.basiclands.Swamp.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Swamp", 263, Rarity.LAND, mage.cards.basiclands.Swamp.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Sweltering Suns", 149, Rarity.RARE, mage.cards.s.SwelteringSuns.class));
        cards.add(new SetCardInfo("Synchronized Strike", 190, Rarity.UNCOMMON, mage.cards.s.SynchronizedStrike.class));
        cards.add(new SetCardInfo("Tah-Crop Elite", 31, Rarity.COMMON, mage.cards.t.TahCropElite.class));
        cards.add(new SetCardInfo("Tah-Crop Skirmisher", 72, Rarity.COMMON, mage.cards.t.TahCropSkirmisher.class));
        cards.add(new SetCardInfo("Tattered Mummy", 278, Rarity.COMMON, mage.cards.t.TatteredMummy.class));
        cards.add(new SetCardInfo("Temmet, Vizier of Naktamun", 207, Rarity.RARE, mage.cards.t.TemmetVizierOfNaktamun.class));
        cards.add(new SetCardInfo("Those Who Serve", 32, Rarity.COMMON, mage.cards.t.ThoseWhoServe.class));
        cards.add(new SetCardInfo("Thresher Lizard", 150, Rarity.COMMON, mage.cards.t.ThresherLizard.class));
        cards.add(new SetCardInfo("Throne of the God-Pharaoh", 237, Rarity.RARE, mage.cards.t.ThroneOfTheGodPharaoh.class));
        cards.add(new SetCardInfo("Timber Gorge", 285, Rarity.COMMON, mage.cards.t.TimberGorge.class));
        cards.add(new SetCardInfo("Time to Reflect", 33, Rarity.UNCOMMON, mage.cards.t.TimeToReflect.class));
        cards.add(new SetCardInfo("Tormenting Voice", 151, Rarity.COMMON, mage.cards.t.TormentingVoice.class));
        cards.add(new SetCardInfo("Tranquil Expanse", 286, Rarity.COMMON, mage.cards.t.TranquilExpanse.class));
        cards.add(new SetCardInfo("Trespasser's Curse", 112, Rarity.COMMON, mage.cards.t.TrespassersCurse.class));
        cards.add(new SetCardInfo("Trial of Ambition", 113, Rarity.UNCOMMON, mage.cards.t.TrialOfAmbition.class));
        cards.add(new SetCardInfo("Trial of Knowledge", 73, Rarity.UNCOMMON, mage.cards.t.TrialOfKnowledge.class));
        cards.add(new SetCardInfo("Trial of Solidarity", 34, Rarity.UNCOMMON, mage.cards.t.TrialOfSolidarity.class));
        cards.add(new SetCardInfo("Trial of Strength", 191, Rarity.UNCOMMON, mage.cards.t.TrialOfStrength.class));
        cards.add(new SetCardInfo("Trial of Zeal", 152, Rarity.UNCOMMON, mage.cards.t.TrialOfZeal.class));
        cards.add(new SetCardInfo("Trueheart Duelist", 35, Rarity.UNCOMMON, mage.cards.t.TrueheartDuelist.class));
        cards.add(new SetCardInfo("Trueheart Twins", 153, Rarity.UNCOMMON, mage.cards.t.TrueheartTwins.class));
        cards.add(new SetCardInfo("Unburden", 114, Rarity.COMMON, mage.cards.u.Unburden.class));
        cards.add(new SetCardInfo("Unwavering Initiate", 36, Rarity.COMMON, mage.cards.u.UnwaveringInitiate.class));
        cards.add(new SetCardInfo("Violent Impact", 154, Rarity.COMMON, mage.cards.v.ViolentImpact.class));
        cards.add(new SetCardInfo("Vizier of Deferment", 37, Rarity.UNCOMMON, mage.cards.v.VizierOfDeferment.class));
        cards.add(new SetCardInfo("Vizier of Many Faces", 74, Rarity.RARE, mage.cards.v.VizierOfManyFaces.class));
        cards.add(new SetCardInfo("Vizier of Remedies", 38, Rarity.UNCOMMON, mage.cards.v.VizierOfRemedies.class));
        cards.add(new SetCardInfo("Vizier of Tumbling Sands", 75, Rarity.UNCOMMON, mage.cards.v.VizierOfTumblingSands.class));
        cards.add(new SetCardInfo("Vizier of the Menagerie", 192, Rarity.MYTHIC, mage.cards.v.VizierOfTheMenagerie.class));
        cards.add(new SetCardInfo("Wander in Death", 115, Rarity.COMMON, mage.cards.w.WanderInDeath.class));
        cards.add(new SetCardInfo("Warfire Javelineer", 155, Rarity.UNCOMMON, mage.cards.w.WarfireJavelineer.class));
        cards.add(new SetCardInfo("Wasteland Scorpion", 116, Rarity.COMMON, mage.cards.w.WastelandScorpion.class));
        cards.add(new SetCardInfo("Watchers of the Dead", 238, Rarity.UNCOMMON, mage.cards.w.WatchersOfTheDead.class));
        cards.add(new SetCardInfo("Watchful Naga", 193, Rarity.UNCOMMON, mage.cards.w.WatchfulNaga.class));
        cards.add(new SetCardInfo("Wayward Servant", 208, Rarity.UNCOMMON, mage.cards.w.WaywardServant.class));
        cards.add(new SetCardInfo("Weaver of Currents", 209, Rarity.UNCOMMON, mage.cards.w.WeaverOfCurrents.class));
        cards.add(new SetCardInfo("Winds of Rebuke", 76, Rarity.COMMON, mage.cards.w.WindsOfRebuke.class));
        cards.add(new SetCardInfo("Winged Shepherd", 39, Rarity.COMMON, mage.cards.w.WingedShepherd.class));
        cards.add(new SetCardInfo("Woodland Stream", 287, Rarity.COMMON, mage.cards.w.WoodlandStream.class));
        cards.add(new SetCardInfo("Zenith Seeker", 77, Rarity.UNCOMMON, mage.cards.z.ZenithSeeker.class));
    }

    @Override
    public List<CardInfo> getSpecialLand() {
        if (savedSpecialLand.isEmpty()) {
            CardCriteria criteria = new CardCriteria();
            criteria.setCodes("MPS-AKH");
            criteria.minCardNumber(1);
            criteria.maxCardNumber(30);
            savedSpecialLand.addAll(CardRepository.instance.findCards(criteria));
        }

        return new ArrayList<>(savedSpecialLand);
    }
}
