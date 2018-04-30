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
package mage.sets;

import mage.cards.ExpansionSet;
import mage.constants.Rarity;
import mage.constants.SetType;

/**
 *
 * @author fireshoes
 */
public class RivalsOfIxalan extends ExpansionSet {

    private static final RivalsOfIxalan instance = new RivalsOfIxalan();

    public static RivalsOfIxalan getInstance() {
        return instance;
    }

    private RivalsOfIxalan() {
        super("Rivals of Ixalan", "RIX", ExpansionSet.buildDate(2018, 1, 19), SetType.EXPANSION);
        this.blockName = "Ixalan";
        this.parentSet = Ixalan.getInstance();
        this.hasBoosters = true;
        this.hasBasicLands = false;
        this.numBoosterLands = 0;
        this.numBoosterCommon = 11;
        this.numBoosterUncommon = 3;
        this.numBoosterRare = 1;
        this.ratioBoosterMythic = 8;
        this.numBoosterDoubleFaced = -1;
        this.maxCardNumberInBooster = 196;

        cards.add(new SetCardInfo("Admiral's Order", 31, Rarity.RARE, mage.cards.a.AdmiralsOrder.class));
        cards.add(new SetCardInfo("Aggressive Urge", 122, Rarity.COMMON, mage.cards.a.AggressiveUrge.class));
        cards.add(new SetCardInfo("Angrath, Minotaur Pirate", 201, Rarity.MYTHIC, mage.cards.a.AngrathMinotaurPirate.class));
        cards.add(new SetCardInfo("Angrath's Ambusher", 202, Rarity.UNCOMMON, mage.cards.a.AngrathsAmbusher.class));
        cards.add(new SetCardInfo("Angrath's Fury", 204, Rarity.RARE, mage.cards.a.AngrathsFury.class));
        cards.add(new SetCardInfo("Angrath, the Flame-Chained", 152, Rarity.MYTHIC, mage.cards.a.AngrathTheFlameChained.class));
        cards.add(new SetCardInfo("Aquatic Incursion", 32, Rarity.UNCOMMON, mage.cards.a.AquaticIncursion.class));
        cards.add(new SetCardInfo("Arch of Orazca", 185, Rarity.RARE, mage.cards.a.ArchOfOrazca.class));
        cards.add(new SetCardInfo("Arterial Flow", 62, Rarity.UNCOMMON, mage.cards.a.ArterialFlow.class));
        cards.add(new SetCardInfo("Atzal, Cave of Eternity", "160b", Rarity.RARE, mage.cards.a.AtzalCaveOfEternity.class));
        cards.add(new SetCardInfo("Atzocan Seer", 153, Rarity.UNCOMMON, mage.cards.a.AtzocanSeer.class));
        cards.add(new SetCardInfo("Awakened Amalgam", 175, Rarity.RARE, mage.cards.a.AwakenedAmalgam.class));
        cards.add(new SetCardInfo("Azor's Gateway", "176a", Rarity.MYTHIC, mage.cards.a.AzorsGateway.class));
        cards.add(new SetCardInfo("Azor, the Lawbringer", 154, Rarity.MYTHIC, mage.cards.a.AzorTheLawbringer.class));
        cards.add(new SetCardInfo("Baffling End", 1, Rarity.UNCOMMON, mage.cards.b.BafflingEnd.class));
        cards.add(new SetCardInfo("Bishop of Binding", 2, Rarity.RARE, mage.cards.b.BishopOfBinding.class));
        cards.add(new SetCardInfo("Blazing Hope", 3, Rarity.UNCOMMON, mage.cards.b.BlazingHope.class));
        cards.add(new SetCardInfo("Blood Sun", 92, Rarity.RARE, mage.cards.b.BloodSun.class));
        cards.add(new SetCardInfo("Bombard", 93, Rarity.COMMON, mage.cards.b.Bombard.class));
        cards.add(new SetCardInfo("Brass's Bounty", 94, Rarity.RARE, mage.cards.b.BrasssBounty.class));
        cards.add(new SetCardInfo("Brazen Freebooter", 95, Rarity.COMMON, mage.cards.b.BrazenFreebooter.class));
        cards.add(new SetCardInfo("Buccaneer's Bravado", 96, Rarity.COMMON, mage.cards.b.BuccaneersBravado.class));
        cards.add(new SetCardInfo("Cacophodon", 123, Rarity.UNCOMMON, mage.cards.c.Cacophodon.class));
        cards.add(new SetCardInfo("Canal Monitor", 63, Rarity.COMMON, mage.cards.c.CanalMonitor.class));
        cards.add(new SetCardInfo("Captain's Hook", 177, Rarity.RARE, mage.cards.c.CaptainsHook.class));
        cards.add(new SetCardInfo("Champion of Dusk", 64, Rarity.RARE, mage.cards.c.ChampionOfDusk.class));
        cards.add(new SetCardInfo("Charging Tuskodon", 97, Rarity.UNCOMMON, mage.cards.c.ChargingTuskodon.class));
        cards.add(new SetCardInfo("Cherished Hatchling", 124, Rarity.UNCOMMON, mage.cards.c.CherishedHatchling.class));
        cards.add(new SetCardInfo("Cinder Barrens", 205, Rarity.COMMON, mage.cards.c.CinderBarrens.class));
        cards.add(new SetCardInfo("Cleansing Ray", 4, Rarity.COMMON, mage.cards.c.CleansingRay.class));
        cards.add(new SetCardInfo("Colossal Dreadmaw", 125, Rarity.COMMON, mage.cards.c.ColossalDreadmaw.class));
        cards.add(new SetCardInfo("Crafty Cutpurse", 33, Rarity.RARE, mage.cards.c.CraftyCutpurse.class));
        cards.add(new SetCardInfo("Crashing Tide", 34, Rarity.COMMON, mage.cards.c.CrashingTide.class));
        cards.add(new SetCardInfo("Crested Herdcaller", 126, Rarity.UNCOMMON, mage.cards.c.CrestedHerdcaller.class));
        cards.add(new SetCardInfo("Curious Obsession", 35, Rarity.UNCOMMON, mage.cards.c.CuriousObsession.class));
        cards.add(new SetCardInfo("Daring Buccaneer", 98, Rarity.UNCOMMON, mage.cards.d.DaringBuccaneer.class));
        cards.add(new SetCardInfo("Dark Inquiry", 65, Rarity.COMMON, mage.cards.d.DarkInquiry.class));
        cards.add(new SetCardInfo("Dead Man's Chest", 66, Rarity.RARE, mage.cards.d.DeadMansChest.class));
        cards.add(new SetCardInfo("Deadeye Brawler", 155, Rarity.UNCOMMON, mage.cards.d.DeadeyeBrawler.class));
        cards.add(new SetCardInfo("Deadeye Rig-Hauler", 36, Rarity.COMMON, mage.cards.d.DeadeyeRigHauler.class));
        cards.add(new SetCardInfo("Deeproot Elite", 127, Rarity.RARE, mage.cards.d.DeeprootElite.class));
        cards.add(new SetCardInfo("Dinosaur Hunter", 67, Rarity.COMMON, mage.cards.d.DinosaurHunter.class));
        cards.add(new SetCardInfo("Dire Fleet Daredevil", 99, Rarity.RARE, mage.cards.d.DireFleetDaredevil.class));
        cards.add(new SetCardInfo("Dire Fleet Neckbreaker", 156, Rarity.UNCOMMON, mage.cards.d.DireFleetNeckbreaker.class));
        cards.add(new SetCardInfo("Dire Fleet Poisoner", 68, Rarity.RARE, mage.cards.d.DireFleetPoisoner.class));
        cards.add(new SetCardInfo("Divine Verdict", 5, Rarity.COMMON, mage.cards.d.DivineVerdict.class));
        cards.add(new SetCardInfo("Dusk Charger", 69, Rarity.COMMON, mage.cards.d.DuskCharger.class));
        cards.add(new SetCardInfo("Dusk Legion Zealot", 70, Rarity.COMMON, mage.cards.d.DuskLegionZealot.class));
        cards.add(new SetCardInfo("Elenda, the Dusk Rose", 157, Rarity.MYTHIC, mage.cards.e.ElendaTheDuskRose.class));
        cards.add(new SetCardInfo("Enter the Unknown", 128, Rarity.UNCOMMON, mage.cards.e.EnterTheUnknown.class));
        cards.add(new SetCardInfo("Etali, Primal Storm", 100, Rarity.RARE, mage.cards.e.EtaliPrimalStorm.class));
        cards.add(new SetCardInfo("Everdawn Champion", 6, Rarity.UNCOMMON, mage.cards.e.EverdawnChampion.class));
        cards.add(new SetCardInfo("Evolving Wilds", 186, Rarity.COMMON, mage.cards.e.EvolvingWilds.class));
        cards.add(new SetCardInfo("Expel from Orazca", 37, Rarity.UNCOMMON, mage.cards.e.ExpelFromOrazca.class));
        cards.add(new SetCardInfo("Exultant Skymarcher", 7, Rarity.COMMON, mage.cards.e.ExultantSkymarcher.class));
        cards.add(new SetCardInfo("Famished Paladin", 8, Rarity.UNCOMMON, mage.cards.f.FamishedPaladin.class));
        cards.add(new SetCardInfo("Fanatical Firebrand", 101, Rarity.COMMON, mage.cards.f.FanaticalFirebrand.class));
        cards.add(new SetCardInfo("Fathom Fleet Boarder", 71, Rarity.COMMON, mage.cards.f.FathomFleetBoarder.class));
        cards.add(new SetCardInfo("Flood of Recollection", 38, Rarity.UNCOMMON, mage.cards.f.FloodOfRecollection.class));
        cards.add(new SetCardInfo("Forerunner of the Coalition", 72, Rarity.UNCOMMON, mage.cards.f.ForerunnerOfTheCoalition.class));
        cards.add(new SetCardInfo("Forerunner of the Empire", 102, Rarity.UNCOMMON, mage.cards.f.ForerunnerOfTheEmpire.class));
        cards.add(new SetCardInfo("Forerunner of the Heralds", 129, Rarity.UNCOMMON, mage.cards.f.ForerunnerOfTheHeralds.class));
        cards.add(new SetCardInfo("Forerunner of the Legion", 9, Rarity.UNCOMMON, mage.cards.f.ForerunnerOfTheLegion.class));
        cards.add(new SetCardInfo("Forest", 196, Rarity.LAND, mage.cards.basiclands.Forest.class));
        cards.add(new SetCardInfo("Form of the Dinosaur", 103, Rarity.RARE, mage.cards.f.FormOfTheDinosaur.class));
        cards.add(new SetCardInfo("Forsaken Sanctuary", 187, Rarity.UNCOMMON, mage.cards.f.ForsakenSanctuary.class));
        cards.add(new SetCardInfo("Foul Orchard", 188, Rarity.UNCOMMON, mage.cards.f.FoulOrchard.class));
        cards.add(new SetCardInfo("Frilled Deathspitter", 104, Rarity.COMMON, mage.cards.f.FrilledDeathspitter.class));
        cards.add(new SetCardInfo("Ghalta, Primal Hunger", 130, Rarity.RARE, mage.cards.g.GhaltaPrimalHunger.class));
        cards.add(new SetCardInfo("Giltgrove Stalker", 131, Rarity.COMMON, mage.cards.g.GiltgroveStalker.class));
        cards.add(new SetCardInfo("Gleaming Barrier", 178, Rarity.COMMON, mage.cards.g.GleamingBarrier.class));
        cards.add(new SetCardInfo("Goblin Trailblazer", 105, Rarity.COMMON, mage.cards.g.GoblinTrailblazer.class));
        cards.add(new SetCardInfo("Golden Demise", 73, Rarity.UNCOMMON, mage.cards.g.GoldenDemise.class));
        cards.add(new SetCardInfo("Golden Guardian", "179a", Rarity.RARE, mage.cards.g.GoldenGuardian.class));
        cards.add(new SetCardInfo("Gold-Forge Garrison", "179b", Rarity.RARE, mage.cards.g.GoldForgeGarrison.class));
        cards.add(new SetCardInfo("Grasping Scoundrel", 74, Rarity.COMMON, mage.cards.g.GraspingScoundrel.class));
        cards.add(new SetCardInfo("Gruesome Fate", 75, Rarity.COMMON, mage.cards.g.GruesomeFate.class));
        cards.add(new SetCardInfo("Hadana's Climb", "158a", Rarity.RARE, mage.cards.h.HadanasClimb.class));
        cards.add(new SetCardInfo("Hardy Veteran", 132, Rarity.COMMON, mage.cards.h.HardyVeteran.class));
        cards.add(new SetCardInfo("Highland Lake", 189, Rarity.UNCOMMON, mage.cards.h.HighlandLake.class));
        cards.add(new SetCardInfo("Hornswoggle", 39, Rarity.UNCOMMON, mage.cards.h.Hornswoggle.class));
        cards.add(new SetCardInfo("Huatli, Radiant Champion", 159, Rarity.MYTHIC, mage.cards.h.HuatliRadiantChampion.class));
        cards.add(new SetCardInfo("Hunt the Weak", 133, Rarity.COMMON, mage.cards.h.HuntTheWeak.class));
        cards.add(new SetCardInfo("Impale", 76, Rarity.COMMON, mage.cards.i.Impale.class));
        cards.add(new SetCardInfo("Imperial Ceratops", 10, Rarity.UNCOMMON, mage.cards.i.ImperialCeratops.class));
        cards.add(new SetCardInfo("Induced Amnesia", 40, Rarity.RARE, mage.cards.i.InducedAmnesia.class));
        cards.add(new SetCardInfo("Island", 193, Rarity.LAND, mage.cards.basiclands.Island.class));
        cards.add(new SetCardInfo("Jade Bearer", 134, Rarity.COMMON, mage.cards.j.JadeBearer.class));
        cards.add(new SetCardInfo("Jadecraft Artisan", 135, Rarity.COMMON, mage.cards.j.JadecraftArtisan.class));
        cards.add(new SetCardInfo("Jadelight Ranger", 136, Rarity.RARE, mage.cards.j.JadelightRanger.class));
        cards.add(new SetCardInfo("Journey to Eternity", "160a", Rarity.RARE, mage.cards.j.JourneyToEternity.class));
        cards.add(new SetCardInfo("Jungle Creeper", 161, Rarity.UNCOMMON, mage.cards.j.JungleCreeper.class));
        cards.add(new SetCardInfo("Jungleborn Pioneer", 137, Rarity.COMMON, mage.cards.j.JunglebornPioneer.class));
        cards.add(new SetCardInfo("Kitesail Corsair", 41, Rarity.COMMON, mage.cards.k.KitesailCorsair.class));
        cards.add(new SetCardInfo("Knight of the Stampede", 138, Rarity.COMMON, mage.cards.k.KnightOfTheStampede.class));
        cards.add(new SetCardInfo("Kumena, Tyrant of Orazca", 162, Rarity.MYTHIC, mage.cards.k.KumenaTyrantOfOrazca.class));
        cards.add(new SetCardInfo("Kumena's Awakening", 42, Rarity.RARE, mage.cards.k.KumenasAwakening.class));
        cards.add(new SetCardInfo("Legion Conquistador", 11, Rarity.COMMON, mage.cards.l.LegionConquistador.class));
        cards.add(new SetCardInfo("Legion Lieutenant", 163, Rarity.UNCOMMON, mage.cards.l.LegionLieutenant.class));
        cards.add(new SetCardInfo("Luminous Bonds", 12, Rarity.COMMON, mage.cards.l.LuminousBonds.class));
        cards.add(new SetCardInfo("Majestic Heliopterus", 13, Rarity.UNCOMMON, mage.cards.m.MajesticHeliopterus.class));
        cards.add(new SetCardInfo("Martyr of Dusk", 14, Rarity.COMMON, mage.cards.m.MartyrOfDusk.class));
        cards.add(new SetCardInfo("Mastermind's Acquisition", 77, Rarity.RARE, mage.cards.m.MastermindsAcquisition.class));
        cards.add(new SetCardInfo("Mausoleum Harpy", 78, Rarity.UNCOMMON, mage.cards.m.MausoleumHarpy.class));
        cards.add(new SetCardInfo("Merfolk Mistbinder", 164, Rarity.UNCOMMON, mage.cards.m.MerfolkMistbinder.class));
        cards.add(new SetCardInfo("Metzali, Tower of Triumph", "165b", Rarity.RARE, mage.cards.m.MetzaliTowerOfTriumph.class));
        cards.add(new SetCardInfo("Mist-Cloaked Herald", 43, Rarity.COMMON, mage.cards.m.MistCloakedHerald.class));
        cards.add(new SetCardInfo("Moment of Craving", 79, Rarity.COMMON, mage.cards.m.MomentOfCraving.class));
        cards.add(new SetCardInfo("Moment of Triumph", 15, Rarity.COMMON, mage.cards.m.MomentOfTriumph.class));
        cards.add(new SetCardInfo("Mountain", 195, Rarity.LAND, mage.cards.basiclands.Mountain.class));
        cards.add(new SetCardInfo("Mutiny", 106, Rarity.COMMON, mage.cards.m.Mutiny.class));
        cards.add(new SetCardInfo("Naturalize", 139, Rarity.COMMON, mage.cards.n.Naturalize.class));
        cards.add(new SetCardInfo("Needletooth Raptor", 107, Rarity.UNCOMMON, mage.cards.n.NeedletoothRaptor.class));
        cards.add(new SetCardInfo("Negate", 44, Rarity.COMMON, mage.cards.n.Negate.class));
        cards.add(new SetCardInfo("Nezahal, Primal Tide", 45, Rarity.RARE, mage.cards.n.NezahalPrimalTide.class));
        cards.add(new SetCardInfo("Oathsworn Vampire", 80, Rarity.UNCOMMON, mage.cards.o.OathswornVampire.class));
        cards.add(new SetCardInfo("Orazca Frillback", 140, Rarity.COMMON, mage.cards.o.OrazcaFrillback.class));
        cards.add(new SetCardInfo("Orazca Raptor", 108, Rarity.COMMON, mage.cards.o.OrazcaRaptor.class));
        cards.add(new SetCardInfo("Orazca Relic", 181, Rarity.COMMON, mage.cards.o.OrazcaRelic.class));
        cards.add(new SetCardInfo("Overgrown Armasaur", 141, Rarity.COMMON, mage.cards.o.OvergrownArmasaur.class));
        cards.add(new SetCardInfo("Paladin of Atonement", 16, Rarity.RARE, mage.cards.p.PaladinOfAtonement.class));
        cards.add(new SetCardInfo("Path of Discovery", 142, Rarity.RARE, mage.cards.p.PathOfDiscovery.class));
        cards.add(new SetCardInfo("Path of Mettle", "165a", Rarity.RARE, mage.cards.p.PathOfMettle.class));
        cards.add(new SetCardInfo("Pirate's Pillage", 109, Rarity.UNCOMMON, mage.cards.p.PiratesPillage.class));
        cards.add(new SetCardInfo("Pitiless Plunderer", 81, Rarity.UNCOMMON, mage.cards.p.PitilessPlunderer.class));
        cards.add(new SetCardInfo("Plains", 192, Rarity.LAND, mage.cards.basiclands.Plains.class));
        cards.add(new SetCardInfo("Plummet", 143, Rarity.COMMON, mage.cards.p.Plummet.class));
        cards.add(new SetCardInfo("Polyraptor", 144, Rarity.MYTHIC, mage.cards.p.Polyraptor.class));
        cards.add(new SetCardInfo("Pride of Conquerors", 17, Rarity.UNCOMMON, mage.cards.p.PrideOfConquerors.class));
        cards.add(new SetCardInfo("Profane Procession", "166a", Rarity.RARE, mage.cards.p.ProfaneProcession.class));
        cards.add(new SetCardInfo("Protean Raider", 167, Rarity.RARE, mage.cards.p.ProteanRaider.class));
        cards.add(new SetCardInfo("Radiant Destiny", 18, Rarity.RARE, mage.cards.r.RadiantDestiny.class));
        cards.add(new SetCardInfo("Raging Regisaur", 168, Rarity.UNCOMMON, mage.cards.r.RagingRegisaur.class));
        cards.add(new SetCardInfo("Raptor Companion", 19, Rarity.COMMON, mage.cards.r.RaptorCompanion.class));
        cards.add(new SetCardInfo("Ravenous Chupacabra", 82, Rarity.UNCOMMON, mage.cards.r.RavenousChupacabra.class));
        cards.add(new SetCardInfo("Reaver Ambush", 83, Rarity.UNCOMMON, mage.cards.r.ReaverAmbush.class));
        cards.add(new SetCardInfo("Reckless Rage", 110, Rarity.UNCOMMON, mage.cards.r.RecklessRage.class));
        cards.add(new SetCardInfo("Recover", 84, Rarity.COMMON, mage.cards.r.Recover.class));
        cards.add(new SetCardInfo("Rekindling Phoenix", 111, Rarity.MYTHIC, mage.cards.r.RekindlingPhoenix.class));
        cards.add(new SetCardInfo("Relentless Raptor", 169, Rarity.UNCOMMON, mage.cards.r.RelentlessRaptor.class));
        cards.add(new SetCardInfo("Resplendent Griffin", 170, Rarity.UNCOMMON, mage.cards.r.ResplendentGriffin.class));
        cards.add(new SetCardInfo("Release to the Wind", 46, Rarity.RARE, mage.cards.r.ReleaseToTheWind.class));
        cards.add(new SetCardInfo("River Darter", 47, Rarity.COMMON, mage.cards.r.RiverDarter.class));
        cards.add(new SetCardInfo("Riverwise Augur", 48, Rarity.UNCOMMON, mage.cards.r.RiverwiseAugur.class));
        cards.add(new SetCardInfo("Sadistic Skymarcher", 85, Rarity.UNCOMMON, mage.cards.s.SadisticSkymarcher.class));
        cards.add(new SetCardInfo("Sailor of Means", 49, Rarity.COMMON, mage.cards.s.SailorOfMeans.class));
        cards.add(new SetCardInfo("Sanctum of the Sun", "176b", Rarity.MYTHIC, mage.cards.s.SanctumOfTheSun.class));
        cards.add(new SetCardInfo("Sanguine Glorifier", 20, Rarity.COMMON, mage.cards.s.SanguineGlorifier.class));
        cards.add(new SetCardInfo("Sea Legs", 50, Rarity.COMMON, mage.cards.s.SeaLegs.class));
        cards.add(new SetCardInfo("Seafloor Oracle", 51, Rarity.RARE, mage.cards.s.SeafloorOracle.class));
        cards.add(new SetCardInfo("Secrets of the Golden City", 52, Rarity.COMMON, mage.cards.s.SecretsOfTheGoldenCity.class));
        cards.add(new SetCardInfo("See Red", 112, Rarity.UNCOMMON, mage.cards.s.SeeRed.class));
        cards.add(new SetCardInfo("Shake the Foundations", 113, Rarity.UNCOMMON, mage.cards.s.ShakeTheFoundations.class));
        cards.add(new SetCardInfo("Shatter", 114, Rarity.COMMON, mage.cards.s.Shatter.class));
        cards.add(new SetCardInfo("Siegehorn Ceratops", 171, Rarity.RARE, mage.cards.s.SiegehornCeratops.class));
        cards.add(new SetCardInfo("Silent Gravestone", 182, Rarity.RARE, mage.cards.s.SilentGravestone.class));
        cards.add(new SetCardInfo("Silverclad Ferocidons", 115, Rarity.RARE, mage.cards.s.SilvercladFerocidons.class));
        cards.add(new SetCardInfo("Silvergill Adept", 53, Rarity.UNCOMMON, mage.cards.s.SilvergillAdept.class));
        cards.add(new SetCardInfo("Siren Reaver", 54, Rarity.UNCOMMON, mage.cards.s.SirenReaver.class));
        cards.add(new SetCardInfo("Skymarcher Aspirant", 21, Rarity.UNCOMMON, mage.cards.s.SkymarcherAspirant.class));
        cards.add(new SetCardInfo("Slaughter the Strong", 22, Rarity.RARE, mage.cards.s.SlaughterTheStrong.class));
        cards.add(new SetCardInfo("Slippery Scoundrel", 55, Rarity.UNCOMMON, mage.cards.s.SlipperyScoundrel.class));
        cards.add(new SetCardInfo("Snubhorn Sentry", 23, Rarity.COMMON, mage.cards.s.SnubhornSentry.class));
        cards.add(new SetCardInfo("Soul of the Rapids", 56, Rarity.COMMON, mage.cards.s.SoulOfTheRapids.class));
        cards.add(new SetCardInfo("Sphinx's Decree", 24, Rarity.RARE, mage.cards.s.SphinxsDecree.class));
        cards.add(new SetCardInfo("Spire Winder", 57, Rarity.COMMON, mage.cards.s.SpireWinder.class));
        cards.add(new SetCardInfo("Squire's Devotion", 25, Rarity.COMMON, mage.cards.s.SquiresDevotion.class));
        cards.add(new SetCardInfo("Stampeding Horncrest", 116, Rarity.COMMON, mage.cards.s.StampedingHorncrest.class));
        cards.add(new SetCardInfo("Stone Quarry", 190, Rarity.UNCOMMON, mage.cards.s.StoneQuarry.class));
        cards.add(new SetCardInfo("Storm Fleet Sprinter", 172, Rarity.UNCOMMON, mage.cards.s.StormFleetSprinter.class));
        cards.add(new SetCardInfo("Storm Fleet Swashbuckler", 117, Rarity.UNCOMMON, mage.cards.s.StormFleetSwashbuckler.class));
        cards.add(new SetCardInfo("Storm the Vault", "173a", Rarity.RARE, mage.cards.s.StormTheVault.class));
        cards.add(new SetCardInfo("Strength of the Pack", 145, Rarity.UNCOMMON, mage.cards.s.StrengthOfThePack.class));
        cards.add(new SetCardInfo("Strider Harness", 183, Rarity.COMMON, mage.cards.s.StriderHarness.class));
        cards.add(new SetCardInfo("Sun Sentinel", 26, Rarity.COMMON, mage.cards.s.SunSentinel.class));
        cards.add(new SetCardInfo("Sun-Collared Raptor", 118, Rarity.COMMON, mage.cards.s.SunCollaredRaptor.class));
        cards.add(new SetCardInfo("Sun-Crested Pterodon", 27, Rarity.COMMON, mage.cards.s.SunCrestedPterodon.class));
        cards.add(new SetCardInfo("Swab Goblin", 203, Rarity.COMMON, mage.cards.s.SwabGoblin.class));
        cards.add(new SetCardInfo("Swaggering Corsair", 119, Rarity.COMMON, mage.cards.s.SwaggeringCorsair.class));
        cards.add(new SetCardInfo("Swamp", 194, Rarity.LAND, mage.cards.basiclands.Swamp.class));
        cards.add(new SetCardInfo("Swift Warden", 146, Rarity.UNCOMMON, mage.cards.s.SwiftWarden.class));
        cards.add(new SetCardInfo("Sworn Guardian", 58, Rarity.COMMON, mage.cards.s.SwornGuardian.class));
        cards.add(new SetCardInfo("Temple Altisaur", 28, Rarity.RARE, mage.cards.t.TempleAltisaur.class));
        cards.add(new SetCardInfo("Tendershoot Dryad", 147, Rarity.RARE, mage.cards.t.TendershootDryad.class));
        cards.add(new SetCardInfo("Tetzimoc, Primal Death", 86, Rarity.RARE, mage.cards.t.TetzimocPrimalDeath.class));
        cards.add(new SetCardInfo("The Immortal Sun", 180, Rarity.MYTHIC, mage.cards.t.TheImmortalSun.class));
        cards.add(new SetCardInfo("Thrashing Brontodon", 148, Rarity.UNCOMMON, mage.cards.t.ThrashingBrontodon.class));
        cards.add(new SetCardInfo("Thunderherd Migration", 149, Rarity.UNCOMMON, mage.cards.t.ThunderherdMigration.class));
        cards.add(new SetCardInfo("Tilonalli's Crown", 120, Rarity.COMMON, mage.cards.t.TilonallisCrown.class));
        cards.add(new SetCardInfo("Tilonalli's Summoner", 121, Rarity.RARE, mage.cards.t.TilonallisSummoner.class));
        cards.add(new SetCardInfo("Timestream Navigator", 59, Rarity.MYTHIC, mage.cards.t.TimestreamNavigator.class));
        cards.add(new SetCardInfo("Tomb of the Dusk Rose", "166b", Rarity.RARE, mage.cards.t.TombOfTheDuskRose.class));
        cards.add(new SetCardInfo("Tomb Robber", 87, Rarity.RARE, mage.cards.t.TombRobber.class));
        cards.add(new SetCardInfo("Trapjaw Tyrant", 29, Rarity.MYTHIC, mage.cards.t.TrapjawTyrant.class));
        cards.add(new SetCardInfo("Traveler's Amulet", 184, Rarity.COMMON, mage.cards.t.TravelersAmulet.class));
        cards.add(new SetCardInfo("Twilight Prophet", 88, Rarity.MYTHIC, mage.cards.t.TwilightProphet.class));
        cards.add(new SetCardInfo("Vampire Champion", 198, Rarity.COMMON, mage.cards.v.VampireChampion.class));
        cards.add(new SetCardInfo("Vampire Revenant", 89, Rarity.COMMON, mage.cards.v.VampireRevenant.class));
        cards.add(new SetCardInfo("Vault of Catlacan", "173b", Rarity.RARE, mage.cards.v.VaultOfCatlacan.class));
        cards.add(new SetCardInfo("Vona's Hunger", 90, Rarity.RARE, mage.cards.v.VonasHunger.class));
        cards.add(new SetCardInfo("Voracious Vampire", 91, Rarity.COMMON, mage.cards.v.VoraciousVampire.class));
        cards.add(new SetCardInfo("Vraska, Scheming Gorgon", 197, Rarity.MYTHIC, mage.cards.v.VraskaSchemingGorgon.class));
        cards.add(new SetCardInfo("Vraska's Conquistador", 199, Rarity.UNCOMMON, mage.cards.v.VraskasConquistador.class));
        cards.add(new SetCardInfo("Vraska's Scorn", 200, Rarity.RARE, mage.cards.v.VraskasScorn.class));
        cards.add(new SetCardInfo("Warkite Marauder", 60, Rarity.RARE, mage.cards.w.WarkiteMarauder.class));
        cards.add(new SetCardInfo("Waterknot", 61, Rarity.COMMON, mage.cards.w.Waterknot.class));
        cards.add(new SetCardInfo("Wayward Swordtooth", 150, Rarity.RARE, mage.cards.w.WaywardSwordtooth.class));
        cards.add(new SetCardInfo("Winged Temple of Orazca", "158b", Rarity.RARE, mage.cards.w.WingedTempleOfOrazca.class));
        cards.add(new SetCardInfo("Woodland Stream", 191, Rarity.UNCOMMON, mage.cards.w.WoodlandStream.class));
        cards.add(new SetCardInfo("World Shaper", 151, Rarity.RARE, mage.cards.w.WorldShaper.class));
        cards.add(new SetCardInfo("Zacama, Primal Calamity", 174, Rarity.MYTHIC, mage.cards.z.ZacamaPrimalCalamity.class));
        cards.add(new SetCardInfo("Zetalpa, Primal Dawn", 30, Rarity.RARE, mage.cards.z.ZetalpaPrimalDawn.class));
    }
}
