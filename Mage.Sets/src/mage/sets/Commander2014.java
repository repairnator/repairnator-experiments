/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.sets;

import mage.cards.ExpansionSet;
import mage.constants.Rarity;
import mage.constants.SetType;

/**
 *
 * @author LevelX2
 */
public class Commander2014 extends ExpansionSet {

    private static final Commander2014 instance = new Commander2014();

    public static Commander2014 getInstance() {
        return instance;
    }

    private Commander2014() {
        super("Commander 2014 Edition", "C14", ExpansionSet.buildDate(2014, 11, 07), SetType.SUPPLEMENTAL);
        this.blockName = "Command Zone";
        cards.add(new SetCardInfo("Abyssal Persecutor", 132, Rarity.MYTHIC, mage.cards.a.AbyssalPersecutor.class));
        cards.add(new SetCardInfo("Adarkar Valkyrie", 63, Rarity.RARE, mage.cards.a.AdarkarValkyrie.class));
        cards.add(new SetCardInfo("Aether Gale", 11, Rarity.RARE, mage.cards.a.AetherGale.class));
        cards.add(new SetCardInfo("Aether Snap", 133, Rarity.RARE, mage.cards.a.AetherSnap.class));
        cards.add(new SetCardInfo("Afterlife", 64, Rarity.UNCOMMON, mage.cards.a.Afterlife.class));
        cards.add(new SetCardInfo("Angelic Field Marshal", 2, Rarity.RARE, mage.cards.a.AngelicFieldMarshal.class));
        cards.add(new SetCardInfo("Angel of the Dire Hour", 1, Rarity.RARE, mage.cards.a.AngelOfTheDireHour.class));
        cards.add(new SetCardInfo("Annihilate", 134, Rarity.UNCOMMON, mage.cards.a.Annihilate.class));
        cards.add(new SetCardInfo("Arcane Lighthouse", 59, Rarity.UNCOMMON, mage.cards.a.ArcaneLighthouse.class));
        cards.add(new SetCardInfo("Argentum Armor", 228, Rarity.RARE, mage.cards.a.ArgentumArmor.class));
        cards.add(new SetCardInfo("Armistice", 65, Rarity.RARE, mage.cards.a.Armistice.class));
        cards.add(new SetCardInfo("Artisan of Kozilek", 62, Rarity.UNCOMMON, mage.cards.a.ArtisanOfKozilek.class));
        cards.add(new SetCardInfo("Assault Suit", 53, Rarity.UNCOMMON, mage.cards.a.AssaultSuit.class));
        cards.add(new SetCardInfo("Azure Mage", 98, Rarity.UNCOMMON, mage.cards.a.AzureMage.class));
        cards.add(new SetCardInfo("Bad Moon", 135, Rarity.RARE, mage.cards.b.BadMoon.class));
        cards.add(new SetCardInfo("Barren Moor", 284, Rarity.COMMON, mage.cards.b.BarrenMoor.class));
        cards.add(new SetCardInfo("Beastmaster Ascension", 186, Rarity.RARE, mage.cards.b.BeastmasterAscension.class));
        cards.add(new SetCardInfo("Beetleback Chief", 171, Rarity.UNCOMMON, mage.cards.b.BeetlebackChief.class));
        cards.add(new SetCardInfo("Benevolent Offering", 3, Rarity.RARE, mage.cards.b.BenevolentOffering.class));
        cards.add(new SetCardInfo("Bitter Feud", 32, Rarity.RARE, mage.cards.b.BitterFeud.class));
        cards.add(new SetCardInfo("Black Sun's Zenith", 136, Rarity.RARE, mage.cards.b.BlackSunsZenith.class));
        cards.add(new SetCardInfo("Blasphemous Act", 172, Rarity.RARE, mage.cards.b.BlasphemousAct.class));
        cards.add(new SetCardInfo("Bloodgift Demon", 137, Rarity.RARE, mage.cards.b.BloodgiftDemon.class));
        cards.add(new SetCardInfo("Bogardan Hellkite", 173, Rarity.MYTHIC, mage.cards.b.BogardanHellkite.class));
        cards.add(new SetCardInfo("Bojuka Bog", 285, Rarity.COMMON, mage.cards.b.BojukaBog.class));
        cards.add(new SetCardInfo("Bonehoard", 229, Rarity.RARE, mage.cards.b.Bonehoard.class));
        cards.add(new SetCardInfo("Bosh, Iron Golem", 230, Rarity.RARE, mage.cards.b.BoshIronGolem.class));
        cards.add(new SetCardInfo("Bottle Gnomes", 231, Rarity.UNCOMMON, mage.cards.b.BottleGnomes.class));
        cards.add(new SetCardInfo("Brave the Elements", 66, Rarity.UNCOMMON, mage.cards.b.BraveTheElements.class));
        cards.add(new SetCardInfo("Breaching Leviathan", 12, Rarity.RARE, mage.cards.b.BreachingLeviathan.class));
        cards.add(new SetCardInfo("Brine Elemental", 99, Rarity.UNCOMMON, mage.cards.b.BrineElemental.class));
        cards.add(new SetCardInfo("Buried Ruin", 286, Rarity.UNCOMMON, mage.cards.b.BuriedRuin.class));
        cards.add(new SetCardInfo("Burnished Hart", 232, Rarity.UNCOMMON, mage.cards.b.BurnishedHart.class));
        cards.add(new SetCardInfo("Butcher of Malakir", 138, Rarity.RARE, mage.cards.b.ButcherOfMalakir.class));
        cards.add(new SetCardInfo("Cackling Counterpart", 100, Rarity.RARE, mage.cards.c.CacklingCounterpart.class));
        cards.add(new SetCardInfo("Caged Sun", 233, Rarity.RARE, mage.cards.c.CagedSun.class));
        cards.add(new SetCardInfo("Call to Mind", 101, Rarity.UNCOMMON, mage.cards.c.CallToMind.class));
        cards.add(new SetCardInfo("Cathars' Crusade", 67, Rarity.RARE, mage.cards.c.CatharsCrusade.class));
        cards.add(new SetCardInfo("Cathodion", 234, Rarity.UNCOMMON, mage.cards.c.Cathodion.class));
        cards.add(new SetCardInfo("Celestial Crusader", 68, Rarity.UNCOMMON, mage.cards.c.CelestialCrusader.class));
        cards.add(new SetCardInfo("Chaos Warp", 174, Rarity.RARE, mage.cards.c.ChaosWarp.class));
        cards.add(new SetCardInfo("Charcoal Diamond", 235, Rarity.UNCOMMON, mage.cards.c.CharcoalDiamond.class));
        cards.add(new SetCardInfo("Collective Unconscious", 187, Rarity.RARE, mage.cards.c.CollectiveUnconscious.class));
        cards.add(new SetCardInfo("Comeuppance", 4, Rarity.RARE, mage.cards.c.Comeuppance.class));
        cards.add(new SetCardInfo("Commander's Sphere", 54, Rarity.COMMON, mage.cards.c.CommandersSphere.class));
        cards.add(new SetCardInfo("Compulsive Research", 102, Rarity.COMMON, mage.cards.c.CompulsiveResearch.class));
        cards.add(new SetCardInfo("Concentrate", 103, Rarity.UNCOMMON, mage.cards.c.Concentrate.class));
        cards.add(new SetCardInfo("Condemn", 69, Rarity.UNCOMMON, mage.cards.c.Condemn.class));
        cards.add(new SetCardInfo("Containment Priest", 5, Rarity.RARE, mage.cards.c.ContainmentPriest.class));
        cards.add(new SetCardInfo("Coral Atoll", 287, Rarity.UNCOMMON, mage.cards.c.CoralAtoll.class));
        cards.add(new SetCardInfo("Creeperhulk", 42, Rarity.RARE, mage.cards.c.Creeperhulk.class));
        cards.add(new SetCardInfo("Crown of Doom", 55, Rarity.RARE, mage.cards.c.CrownOfDoom.class));
        cards.add(new SetCardInfo("Crypt Ghast", 139, Rarity.RARE, mage.cards.c.CryptGhast.class));
        cards.add(new SetCardInfo("Crypt of Agadeem", 288, Rarity.RARE, mage.cards.c.CryptOfAgadeem.class));
        cards.add(new SetCardInfo("Crystal Vein", 289, Rarity.UNCOMMON, mage.cards.c.CrystalVein.class));
        cards.add(new SetCardInfo("Cyclonic Rift", 104, Rarity.RARE, mage.cards.c.CyclonicRift.class));
        cards.add(new SetCardInfo("Daretti, Scrap Savant", 33, Rarity.MYTHIC, mage.cards.d.DarettiScrapSavant.class));
        cards.add(new SetCardInfo("Darksteel Citadel", 290, Rarity.UNCOMMON, mage.cards.d.DarksteelCitadel.class));
        cards.add(new SetCardInfo("Decree of Justice", 70, Rarity.RARE, mage.cards.d.DecreeOfJustice.class));
        cards.add(new SetCardInfo("Deep-Sea Kraken", 105, Rarity.RARE, mage.cards.d.DeepSeaKraken.class));
        cards.add(new SetCardInfo("Demon of Wailing Agonies", 21, Rarity.RARE, mage.cards.d.DemonOfWailingAgonies.class));
        cards.add(new SetCardInfo("Deploy to the Front", 6, Rarity.RARE, mage.cards.d.DeployToTheFront.class));
        cards.add(new SetCardInfo("Desert Twister", 188, Rarity.UNCOMMON, mage.cards.d.DesertTwister.class));
        cards.add(new SetCardInfo("Disciple of Bolas", 140, Rarity.RARE, mage.cards.d.DiscipleOfBolas.class));
        cards.add(new SetCardInfo("Dismiss", 106, Rarity.UNCOMMON, mage.cards.d.Dismiss.class));
        cards.add(new SetCardInfo("Distorting Wake", 107, Rarity.RARE, mage.cards.d.DistortingWake.class));
        cards.add(new SetCardInfo("Domineering Will", 13, Rarity.RARE, mage.cards.d.DomineeringWill.class));
        cards.add(new SetCardInfo("Dormant Volcano", 291, Rarity.UNCOMMON, mage.cards.d.DormantVolcano.class));
        cards.add(new SetCardInfo("Drana, Kalastria Bloodchief", 141, Rarity.RARE, mage.cards.d.DranaKalastriaBloodchief.class));
        cards.add(new SetCardInfo("Dread Return", 142, Rarity.UNCOMMON, mage.cards.d.DreadReturn.class));
        cards.add(new SetCardInfo("Dreamstone Hedron", 236, Rarity.UNCOMMON, mage.cards.d.DreamstoneHedron.class));
        cards.add(new SetCardInfo("Dregs of Sorrow", 143, Rarity.RARE, mage.cards.d.DregsOfSorrow.class));
        cards.add(new SetCardInfo("Drifting Meadow", 292, Rarity.COMMON, mage.cards.d.DriftingMeadow.class));
        cards.add(new SetCardInfo("Drove of Elves", 189, Rarity.UNCOMMON, mage.cards.d.DroveOfElves.class));
        cards.add(new SetCardInfo("Dualcaster Mage", 34, Rarity.RARE, mage.cards.d.DualcasterMage.class));
        cards.add(new SetCardInfo("Dulcet Sirens", 14, Rarity.RARE, mage.cards.d.DulcetSirens.class));
        cards.add(new SetCardInfo("Elvish Archdruid", 190, Rarity.RARE, mage.cards.e.ElvishArchdruid.class));
        cards.add(new SetCardInfo("Elvish Mystic", 191, Rarity.COMMON, mage.cards.e.ElvishMystic.class));
        cards.add(new SetCardInfo("Elvish Skysweeper", 192, Rarity.COMMON, mage.cards.e.ElvishSkysweeper.class));
        cards.add(new SetCardInfo("Elvish Visionary", 193, Rarity.COMMON, mage.cards.e.ElvishVisionary.class));
        cards.add(new SetCardInfo("Emerald Medallion", 237, Rarity.RARE, mage.cards.e.EmeraldMedallion.class));
        cards.add(new SetCardInfo("Emeria, the Sky Ruin", 293, Rarity.RARE, mage.cards.e.EmeriaTheSkyRuin.class));
        cards.add(new SetCardInfo("Epochrasite", 238, Rarity.RARE, mage.cards.e.Epochrasite.class));
        cards.add(new SetCardInfo("Essence Warden", 194, Rarity.COMMON, mage.cards.e.EssenceWarden.class));
        cards.add(new SetCardInfo("Everflowing Chalice", 239, Rarity.UNCOMMON, mage.cards.e.EverflowingChalice.class));
        cards.add(new SetCardInfo("Everglades", 294, Rarity.UNCOMMON, mage.cards.e.Everglades.class));
        cards.add(new SetCardInfo("Evernight Shade", 144, Rarity.UNCOMMON, mage.cards.e.EvernightShade.class));
        cards.add(new SetCardInfo("Evolving Wilds", 295, Rarity.COMMON, mage.cards.e.EvolvingWilds.class));
        cards.add(new SetCardInfo("Exclude", 108, Rarity.COMMON, mage.cards.e.Exclude.class));
        cards.add(new SetCardInfo("Ezuri, Renegade Leader", 195, Rarity.RARE, mage.cards.e.EzuriRenegadeLeader.class));
        cards.add(new SetCardInfo("Faithless Looting", 175, Rarity.COMMON, mage.cards.f.FaithlessLooting.class));
        cards.add(new SetCardInfo("Farhaven Elf", 196, Rarity.COMMON, mage.cards.f.FarhavenElf.class));
        cards.add(new SetCardInfo("Fathom Seer", 109, Rarity.COMMON, mage.cards.f.FathomSeer.class));
        cards.add(new SetCardInfo("Feldon of the Third Path", 35, Rarity.MYTHIC, mage.cards.f.FeldonOfTheThirdPath.class));
        cards.add(new SetCardInfo("Fell the Mighty", 7, Rarity.RARE, mage.cards.f.FellTheMighty.class));
        cards.add(new SetCardInfo("Fire Diamond", 240, Rarity.UNCOMMON, mage.cards.f.FireDiamond.class));
        cards.add(new SetCardInfo("Flamekin Village", 60, Rarity.RARE, mage.cards.f.FlamekinVillage.class));
        cards.add(new SetCardInfo("Flametongue Kavu", 176, Rarity.UNCOMMON, mage.cards.f.FlametongueKavu.class));
        cards.add(new SetCardInfo("Flesh Carver", 22, Rarity.RARE, mage.cards.f.FleshCarver.class));
        cards.add(new SetCardInfo("Flickerwisp", 71, Rarity.UNCOMMON, mage.cards.f.Flickerwisp.class));
        cards.add(new SetCardInfo("Fog Bank", 110, Rarity.UNCOMMON, mage.cards.f.FogBank.class));
        cards.add(new SetCardInfo("Fool's Demise", 111, Rarity.UNCOMMON, mage.cards.f.FoolsDemise.class));
        cards.add(new SetCardInfo("Forest", 334, Rarity.LAND, mage.cards.basiclands.Forest.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Forest", 335, Rarity.LAND, mage.cards.basiclands.Forest.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Forest", 336, Rarity.LAND, mage.cards.basiclands.Forest.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Forest", 337, Rarity.LAND, mage.cards.basiclands.Forest.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Forgotten Cave", 296, Rarity.COMMON, mage.cards.f.ForgottenCave.class));
        cards.add(new SetCardInfo("Fresh Meat", 197, Rarity.RARE, mage.cards.f.FreshMeat.class));
        cards.add(new SetCardInfo("Freyalise, Llanowar's Fury", 43, Rarity.MYTHIC, mage.cards.f.FreyaliseLlanowarsFury.class));
        cards.add(new SetCardInfo("Frost Titan", 112, Rarity.MYTHIC, mage.cards.f.FrostTitan.class));
        cards.add(new SetCardInfo("Gargoyle Castle", 297, Rarity.RARE, mage.cards.g.GargoyleCastle.class));
        cards.add(new SetCardInfo("Geist-Honored Monk", 72, Rarity.RARE, mage.cards.g.GeistHonoredMonk.class));
        cards.add(new SetCardInfo("Ghost Quarter", 298, Rarity.UNCOMMON, mage.cards.g.GhostQuarter.class));
        cards.add(new SetCardInfo("Ghoulcaller Gisa", 23, Rarity.MYTHIC, mage.cards.g.GhoulcallerGisa.class));
        cards.add(new SetCardInfo("Gift of Estates", 73, Rarity.UNCOMMON, mage.cards.g.GiftOfEstates.class));
        cards.add(new SetCardInfo("Goblin Welder", 177, Rarity.RARE, mage.cards.g.GoblinWelder.class));
        cards.add(new SetCardInfo("Grand Abolisher", 74, Rarity.RARE, mage.cards.g.GrandAbolisher.class));
        cards.add(new SetCardInfo("Grave Sifter", 44, Rarity.RARE, mage.cards.g.GraveSifter.class));
        cards.add(new SetCardInfo("Grave Titan", 145, Rarity.MYTHIC, mage.cards.g.GraveTitan.class));
        cards.add(new SetCardInfo("Gray Merchant of Asphodel", 146, Rarity.COMMON, mage.cards.g.GrayMerchantOfAsphodel.class));
        cards.add(new SetCardInfo("Great Furnace", 299, Rarity.COMMON, mage.cards.g.GreatFurnace.class));
        cards.add(new SetCardInfo("Grim Flowering", 198, Rarity.UNCOMMON, mage.cards.g.GrimFlowering.class));
        cards.add(new SetCardInfo("Hallowed Spiritkeeper", 8, Rarity.RARE, mage.cards.h.HallowedSpiritkeeper.class));
        cards.add(new SetCardInfo("Harrow", 199, Rarity.COMMON, mage.cards.h.Harrow.class));
        cards.add(new SetCardInfo("Haunted Fengraf", 300, Rarity.COMMON, mage.cards.h.HauntedFengraf.class));
        cards.add(new SetCardInfo("Havenwood Battleground", 301, Rarity.UNCOMMON, mage.cards.h.HavenwoodBattleground.class));
        cards.add(new SetCardInfo("Hoard-Smelter Dragon", 178, Rarity.RARE, mage.cards.h.HoardSmelterDragon.class));
        cards.add(new SetCardInfo("Hoverguard Sweepers", 113, Rarity.RARE, mage.cards.h.HoverguardSweepers.class));
        cards.add(new SetCardInfo("Hunting Triad", 200, Rarity.UNCOMMON, mage.cards.h.HuntingTriad.class));
        cards.add(new SetCardInfo("Ichor Wellspring", 241, Rarity.COMMON, mage.cards.i.IchorWellspring.class));
        cards.add(new SetCardInfo("Immaculate Magistrate", 201, Rarity.RARE, mage.cards.i.ImmaculateMagistrate.class));
        cards.add(new SetCardInfo("Impact Resonance", 36, Rarity.RARE, mage.cards.i.ImpactResonance.class));
        cards.add(new SetCardInfo("Imperious Perfect", 202, Rarity.UNCOMMON, mage.cards.i.ImperiousPerfect.class));
        cards.add(new SetCardInfo("Incite Rebellion", 37, Rarity.RARE, mage.cards.i.InciteRebellion.class));
        cards.add(new SetCardInfo("Infernal Offering", 24, Rarity.RARE, mage.cards.i.InfernalOffering.class));
        cards.add(new SetCardInfo("Infinite Reflection", 114, Rarity.RARE, mage.cards.i.InfiniteReflection.class));
        cards.add(new SetCardInfo("Ingot Chewer", 179, Rarity.COMMON, mage.cards.i.IngotChewer.class));
        cards.add(new SetCardInfo("Intellectual Offering", 15, Rarity.RARE, mage.cards.i.IntellectualOffering.class));
        cards.add(new SetCardInfo("Into the Roil", 115, Rarity.COMMON, mage.cards.i.IntoTheRoil.class));
        cards.add(new SetCardInfo("Island", 322, Rarity.LAND, mage.cards.basiclands.Island.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Island", 323, Rarity.LAND, mage.cards.basiclands.Island.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Island", 324, Rarity.LAND, mage.cards.basiclands.Island.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Island", 325, Rarity.LAND, mage.cards.basiclands.Island.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Ixidron", 116, Rarity.RARE, mage.cards.i.Ixidron.class));
        cards.add(new SetCardInfo("Jalum Tome", 242, Rarity.RARE, mage.cards.j.JalumTome.class));
        cards.add(new SetCardInfo("Jazal Goldmane", 9, Rarity.MYTHIC, mage.cards.j.JazalGoldmane.class));
        cards.add(new SetCardInfo("Jet Medallion", 243, Rarity.RARE, mage.cards.j.JetMedallion.class));
        cards.add(new SetCardInfo("Joraga Warcaller", 203, Rarity.RARE, mage.cards.j.JoragaWarcaller.class));
        cards.add(new SetCardInfo("Jungle Basin", 302, Rarity.UNCOMMON, mage.cards.j.JungleBasin.class));
        cards.add(new SetCardInfo("Junk Diver", 244, Rarity.RARE, mage.cards.j.JunkDiver.class));
        cards.add(new SetCardInfo("Karoo", 303, Rarity.UNCOMMON, mage.cards.k.Karoo.class));
        cards.add(new SetCardInfo("Kemba, Kha Regent", 75, Rarity.RARE, mage.cards.k.KembaKhaRegent.class));
        cards.add(new SetCardInfo("Kor Sanctifiers", 76, Rarity.COMMON, mage.cards.k.KorSanctifiers.class));
        cards.add(new SetCardInfo("Lashwrithe", 245, Rarity.RARE, mage.cards.l.Lashwrithe.class));
        cards.add(new SetCardInfo("Lifeblood Hydra", 45, Rarity.RARE, mage.cards.l.LifebloodHydra.class));
        cards.add(new SetCardInfo("Liliana's Reaver", 147, Rarity.RARE, mage.cards.l.LilianasReaver.class));
        cards.add(new SetCardInfo("Liquimetal Coating", 246, Rarity.UNCOMMON, mage.cards.l.LiquimetalCoating.class));
        cards.add(new SetCardInfo("Llanowar Elves", 204, Rarity.COMMON, mage.cards.l.LlanowarElves.class));
        cards.add(new SetCardInfo("Lonely Sandbar", 304, Rarity.COMMON, mage.cards.l.LonelySandbar.class));
        cards.add(new SetCardInfo("Loreseeker's Stone", 56, Rarity.UNCOMMON, mage.cards.l.LoreseekersStone.class));
        cards.add(new SetCardInfo("Lorthos, the Tidemaker", 117, Rarity.MYTHIC, mage.cards.l.LorthosTheTidemaker.class));
        cards.add(new SetCardInfo("Loxodon Warhammer", 247, Rarity.RARE, mage.cards.l.LoxodonWarhammer.class));
        cards.add(new SetCardInfo("Lys Alana Huntmaster", 205, Rarity.COMMON, mage.cards.l.LysAlanaHuntmaster.class));
        cards.add(new SetCardInfo("Magmaquake", 180, Rarity.RARE, mage.cards.m.Magmaquake.class));
        cards.add(new SetCardInfo("Magus of the Coffers", 148, Rarity.RARE, mage.cards.m.MagusOfTheCoffers.class));
        cards.add(new SetCardInfo("Malicious Affliction", 25, Rarity.RARE, mage.cards.m.MaliciousAffliction.class));
        cards.add(new SetCardInfo("Marble Diamond", 248, Rarity.UNCOMMON, mage.cards.m.MarbleDiamond.class));
        cards.add(new SetCardInfo("Marshal's Anthem", 77, Rarity.RARE, mage.cards.m.MarshalsAnthem.class));
        cards.add(new SetCardInfo("Martial Coup", 78, Rarity.RARE, mage.cards.m.MartialCoup.class));
        cards.add(new SetCardInfo("Masked Admirers", 206, Rarity.RARE, mage.cards.m.MaskedAdmirers.class));
        cards.add(new SetCardInfo("Mask of Memory", 249, Rarity.UNCOMMON, mage.cards.m.MaskOfMemory.class));
        cards.add(new SetCardInfo("Masterwork of Ingenuity", 57, Rarity.RARE, mage.cards.m.MasterworkOfIngenuity.class));
        cards.add(new SetCardInfo("Mentor of the Meek", 79, Rarity.RARE, mage.cards.m.MentorOfTheMeek.class));
        cards.add(new SetCardInfo("Midnight Haunting", 80, Rarity.UNCOMMON, mage.cards.m.MidnightHaunting.class));
        cards.add(new SetCardInfo("Mind Stone", 250, Rarity.UNCOMMON, mage.cards.m.MindStone.class));
        cards.add(new SetCardInfo("Mobilization", 81, Rarity.RARE, mage.cards.m.Mobilization.class));
        cards.add(new SetCardInfo("Moonsilver Spear", 251, Rarity.RARE, mage.cards.m.MoonsilverSpear.class));
        cards.add(new SetCardInfo("Morkrut Banshee", 149, Rarity.UNCOMMON, mage.cards.m.MorkrutBanshee.class));
        cards.add(new SetCardInfo("Moss Diamond", 252, Rarity.UNCOMMON, mage.cards.m.MossDiamond.class));
        cards.add(new SetCardInfo("Mountain", 330, Rarity.LAND, mage.cards.basiclands.Mountain.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Mountain", 331, Rarity.LAND, mage.cards.basiclands.Mountain.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Mountain", 332, Rarity.LAND, mage.cards.basiclands.Mountain.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Mountain", 333, Rarity.LAND, mage.cards.basiclands.Mountain.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Mulldrifter", 118, Rarity.COMMON, mage.cards.m.Mulldrifter.class));
        cards.add(new SetCardInfo("Mutilate", 150, Rarity.RARE, mage.cards.m.Mutilate.class));
        cards.add(new SetCardInfo("Mycosynth Wellspring", 253, Rarity.COMMON, mage.cards.m.MycosynthWellspring.class));
        cards.add(new SetCardInfo("Myr Battlesphere", 254, Rarity.RARE, mage.cards.m.MyrBattlesphere.class));
        cards.add(new SetCardInfo("Myriad Landscape", 61, Rarity.UNCOMMON, mage.cards.m.MyriadLandscape.class));
        cards.add(new SetCardInfo("Myr Retriever", 255, Rarity.UNCOMMON, mage.cards.m.MyrRetriever.class));
        cards.add(new SetCardInfo("Myr Sire", 256, Rarity.COMMON, mage.cards.m.MyrSire.class));
        cards.add(new SetCardInfo("Nahiri, the Lithomancer", 10, Rarity.MYTHIC, mage.cards.n.NahiriTheLithomancer.class));
        cards.add(new SetCardInfo("Nantuko Shade", 151, Rarity.RARE, mage.cards.n.NantukoShade.class));
        cards.add(new SetCardInfo("Necromantic Selection", 26, Rarity.RARE, mage.cards.n.NecromanticSelection.class));
        cards.add(new SetCardInfo("Nekrataal", 152, Rarity.UNCOMMON, mage.cards.n.Nekrataal.class));
        cards.add(new SetCardInfo("Nevinyrral's Disk", 257, Rarity.RARE, mage.cards.n.NevinyrralsDisk.class));
        cards.add(new SetCardInfo("Nomads' Assembly", 82, Rarity.RARE, mage.cards.n.NomadsAssembly.class));
        cards.add(new SetCardInfo("Oblation", 83, Rarity.RARE, mage.cards.o.Oblation.class));
        cards.add(new SetCardInfo("Ob Nixilis of the Black Oath", 27, Rarity.MYTHIC, mage.cards.o.ObNixilisOfTheBlackOath.class));
        cards.add(new SetCardInfo("Oran-Rief, the Vastwood", 305, Rarity.RARE, mage.cards.o.OranRiefTheVastwood.class));
        cards.add(new SetCardInfo("Overrun", 207, Rarity.UNCOMMON, mage.cards.o.Overrun.class));
        cards.add(new SetCardInfo("Overseer of the Damned", 28, Rarity.RARE, mage.cards.o.OverseerOfTheDamned.class));
        cards.add(new SetCardInfo("Overwhelming Stampede", 208, Rarity.RARE, mage.cards.o.OverwhelmingStampede.class));
        cards.add(new SetCardInfo("Palladium Myr", 258, Rarity.UNCOMMON, mage.cards.p.PalladiumMyr.class));
        cards.add(new SetCardInfo("Panic Spellbomb", 259, Rarity.COMMON, mage.cards.p.PanicSpellbomb.class));
        cards.add(new SetCardInfo("Pearl Medallion", 260, Rarity.RARE, mage.cards.p.PearlMedallion.class));
        cards.add(new SetCardInfo("Pentavus", 261, Rarity.RARE, mage.cards.p.Pentavus.class));
        cards.add(new SetCardInfo("Pestilence Demon", 153, Rarity.RARE, mage.cards.p.PestilenceDemon.class));
        cards.add(new SetCardInfo("Phyrexian Gargantua", 154, Rarity.UNCOMMON, mage.cards.p.PhyrexianGargantua.class));
        cards.add(new SetCardInfo("Phyrexian Ingester", 119, Rarity.RARE, mage.cards.p.PhyrexianIngester.class));
        cards.add(new SetCardInfo("Phyrexia's Core", 306, Rarity.UNCOMMON, mage.cards.p.PhyrexiasCore.class));
        cards.add(new SetCardInfo("Pilgrim's Eye", 262, Rarity.COMMON, mage.cards.p.PilgrimsEye.class));
        cards.add(new SetCardInfo("Plains", 318, Rarity.LAND, mage.cards.basiclands.Plains.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Plains", 319, Rarity.LAND, mage.cards.basiclands.Plains.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Plains", 320, Rarity.LAND, mage.cards.basiclands.Plains.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Plains", 321, Rarity.LAND, mage.cards.basiclands.Plains.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Polluted Mire", 307, Rarity.COMMON, mage.cards.p.PollutedMire.class));
        cards.add(new SetCardInfo("Pongify", 120, Rarity.UNCOMMON, mage.cards.p.Pongify.class));
        cards.add(new SetCardInfo("Pontiff of Blight", 155, Rarity.RARE, mage.cards.p.PontiffOfBlight.class));
        cards.add(new SetCardInfo("Praetor's Counsel", 209, Rarity.MYTHIC, mage.cards.p.PraetorsCounsel.class));
        cards.add(new SetCardInfo("Predator, Flagship", 263, Rarity.RARE, mage.cards.p.PredatorFlagship.class));
        cards.add(new SetCardInfo("Priest of Titania", 210, Rarity.COMMON, mage.cards.p.PriestOfTitania.class));
        cards.add(new SetCardInfo("Primordial Sage", 211, Rarity.RARE, mage.cards.p.PrimordialSage.class));
        cards.add(new SetCardInfo("Pristine Talisman", 264, Rarity.COMMON, mage.cards.p.PristineTalisman.class));
        cards.add(new SetCardInfo("Profane Command", 156, Rarity.RARE, mage.cards.p.ProfaneCommand.class));
        cards.add(new SetCardInfo("Promise of Power", 157, Rarity.RARE, mage.cards.p.PromiseOfPower.class));
        cards.add(new SetCardInfo("Rampaging Baloths", 212, Rarity.MYTHIC, mage.cards.r.RampagingBaloths.class));
        cards.add(new SetCardInfo("Raving Dead", 29, Rarity.RARE, mage.cards.r.RavingDead.class));
        cards.add(new SetCardInfo("Read the Bones", 158, Rarity.COMMON, mage.cards.r.ReadTheBones.class));
        cards.add(new SetCardInfo("Reaper from the Abyss", 159, Rarity.MYTHIC, mage.cards.r.ReaperFromTheAbyss.class));
        cards.add(new SetCardInfo("Reclamation Sage", 213, Rarity.UNCOMMON, mage.cards.r.ReclamationSage.class));
        cards.add(new SetCardInfo("Reef Worm", 16, Rarity.RARE, mage.cards.r.ReefWorm.class));
        cards.add(new SetCardInfo("Reliquary Tower", 308, Rarity.UNCOMMON, mage.cards.r.ReliquaryTower.class));
        cards.add(new SetCardInfo("Remote Isle", 309, Rarity.COMMON, mage.cards.r.RemoteIsle.class));
        cards.add(new SetCardInfo("Requiem Angel", 84, Rarity.RARE, mage.cards.r.RequiemAngel.class));
        cards.add(new SetCardInfo("Return to Dust", 85, Rarity.UNCOMMON, mage.cards.r.ReturnToDust.class));
        cards.add(new SetCardInfo("Riptide Survivor", 121, Rarity.UNCOMMON, mage.cards.r.RiptideSurvivor.class));
        cards.add(new SetCardInfo("Rite of Replication", 122, Rarity.RARE, mage.cards.r.RiteOfReplication.class));
        cards.add(new SetCardInfo("Ruby Medallion", 265, Rarity.RARE, mage.cards.r.RubyMedallion.class));
        cards.add(new SetCardInfo("Rush of Knowledge", 123, Rarity.COMMON, mage.cards.r.RushOfKnowledge.class));
        cards.add(new SetCardInfo("Sacred Mesa", 86, Rarity.RARE, mage.cards.s.SacredMesa.class));
        cards.add(new SetCardInfo("Sapphire Medallion", 266, Rarity.RARE, mage.cards.s.SapphireMedallion.class));
        cards.add(new SetCardInfo("Scrap Mastery", 38, Rarity.RARE, mage.cards.s.ScrapMastery.class));
        cards.add(new SetCardInfo("Sea Gate Oracle", 124, Rarity.COMMON, mage.cards.s.SeaGateOracle.class));
        cards.add(new SetCardInfo("Secluded Steppe", 310, Rarity.COMMON, mage.cards.s.SecludedSteppe.class));
        cards.add(new SetCardInfo("Seer's Sundial", 267, Rarity.RARE, mage.cards.s.SeersSundial.class));
        cards.add(new SetCardInfo("Serra Avatar", 87, Rarity.MYTHIC, mage.cards.s.SerraAvatar.class));
        cards.add(new SetCardInfo("Shaper Parasite", 125, Rarity.COMMON, mage.cards.s.ShaperParasite.class));
        cards.add(new SetCardInfo("Shriekmaw", 160, Rarity.UNCOMMON, mage.cards.s.Shriekmaw.class));
        cards.add(new SetCardInfo("Siege Behemoth", 46, Rarity.RARE, mage.cards.s.SiegeBehemoth.class));
        cards.add(new SetCardInfo("Sign in Blood", 161, Rarity.COMMON, mage.cards.s.SignInBlood.class));
        cards.add(new SetCardInfo("Silklash Spider", 214, Rarity.RARE, mage.cards.s.SilklashSpider.class));
        cards.add(new SetCardInfo("Silverblade Paladin", 88, Rarity.RARE, mage.cards.s.SilverbladePaladin.class));
        cards.add(new SetCardInfo("Skeletal Scrying", 162, Rarity.UNCOMMON, mage.cards.s.SkeletalScrying.class));
        cards.add(new SetCardInfo("Skirsdag High Priest", 163, Rarity.RARE, mage.cards.s.SkirsdagHighPriest.class));
        cards.add(new SetCardInfo("Skullclamp", 268, Rarity.UNCOMMON, mage.cards.s.Skullclamp.class));
        cards.add(new SetCardInfo("Sky Diamond", 269, Rarity.UNCOMMON, mage.cards.s.SkyDiamond.class));
        cards.add(new SetCardInfo("Skyhunter Skirmisher", 89, Rarity.UNCOMMON, mage.cards.s.SkyhunterSkirmisher.class));
        cards.add(new SetCardInfo("Slippery Karst", 311, Rarity.COMMON, mage.cards.s.SlipperyKarst.class));
        cards.add(new SetCardInfo("Smoldering Crater", 312, Rarity.COMMON, mage.cards.s.SmolderingCrater.class));
        cards.add(new SetCardInfo("Solemn Simulacrum", 271, Rarity.RARE, mage.cards.s.SolemnSimulacrum.class));
        cards.add(new SetCardInfo("Sol Ring", 270, Rarity.UNCOMMON, mage.cards.s.SolRing.class));
        cards.add(new SetCardInfo("Song of the Dryads", 47, Rarity.RARE, mage.cards.s.SongOfTheDryads.class));
        cards.add(new SetCardInfo("Soul of the Harvest", 215, Rarity.RARE, mage.cards.s.SoulOfTheHarvest.class));
        cards.add(new SetCardInfo("Spectral Procession", 90, Rarity.UNCOMMON, mage.cards.s.SpectralProcession.class));
        cards.add(new SetCardInfo("Sphinx of Jwar Isle", 126, Rarity.RARE, mage.cards.s.SphinxOfJwarIsle.class));
        cards.add(new SetCardInfo("Sphinx of Magosi", 127, Rarity.RARE, mage.cards.s.SphinxOfMagosi.class));
        cards.add(new SetCardInfo("Sphinx of Uthuun", 128, Rarity.RARE, mage.cards.s.SphinxOfUthuun.class));
        cards.add(new SetCardInfo("Spine of Ish Sah", 272, Rarity.RARE, mage.cards.s.SpineOfIshSah.class));
        cards.add(new SetCardInfo("Spitebellows", 181, Rarity.UNCOMMON, mage.cards.s.Spitebellows.class));
        cards.add(new SetCardInfo("Spoils of Blood", 30, Rarity.RARE, mage.cards.s.SpoilsOfBlood.class));
        cards.add(new SetCardInfo("Starstorm", 182, Rarity.RARE, mage.cards.s.Starstorm.class));
        cards.add(new SetCardInfo("Steel Hellkite", 273, Rarity.RARE, mage.cards.s.SteelHellkite.class));
        cards.add(new SetCardInfo("Stitcher Geralf", 17, Rarity.MYTHIC, mage.cards.s.StitcherGeralf.class));
        cards.add(new SetCardInfo("Stormsurge Kraken", 18, Rarity.RARE, mage.cards.s.StormsurgeKraken.class));
        cards.add(new SetCardInfo("Strata Scythe", 274, Rarity.RARE, mage.cards.s.StrataScythe.class));
        cards.add(new SetCardInfo("Stroke of Genius", 129, Rarity.RARE, mage.cards.s.StrokeOfGenius.class));
        cards.add(new SetCardInfo("Sudden Spoiling", 164, Rarity.RARE, mage.cards.s.SuddenSpoiling.class));
        cards.add(new SetCardInfo("Sunblast Angel", 92, Rarity.RARE, mage.cards.s.SunblastAngel.class));
        cards.add(new SetCardInfo("Sun Titan", 91, Rarity.MYTHIC, mage.cards.s.SunTitan.class));
        cards.add(new SetCardInfo("Swamp", 326, Rarity.LAND, mage.cards.basiclands.Swamp.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Swamp", 327, Rarity.LAND, mage.cards.basiclands.Swamp.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Swamp", 328, Rarity.LAND, mage.cards.basiclands.Swamp.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Swamp", 329, Rarity.LAND, mage.cards.basiclands.Swamp.class, NON_FULL_USE_VARIOUS));
        cards.add(new SetCardInfo("Swiftfoot Boots", 275, Rarity.UNCOMMON, mage.cards.s.SwiftfootBoots.class));
        cards.add(new SetCardInfo("Sword of Vengeance", 276, Rarity.RARE, mage.cards.s.SwordOfVengeance.class));
        cards.add(new SetCardInfo("Sylvan Offering", 48, Rarity.RARE, mage.cards.s.SylvanOffering.class));
        cards.add(new SetCardInfo("Sylvan Ranger", 216, Rarity.COMMON, mage.cards.s.SylvanRanger.class));
        cards.add(new SetCardInfo("Sylvan Safekeeper", 217, Rarity.RARE, mage.cards.s.SylvanSafekeeper.class));
        cards.add(new SetCardInfo("Syphon Mind", 165, Rarity.COMMON, mage.cards.s.SyphonMind.class));
        cards.add(new SetCardInfo("Tectonic Edge", 313, Rarity.UNCOMMON, mage.cards.t.TectonicEdge.class));
        cards.add(new SetCardInfo("Teferi, Temporal Archmage", 19, Rarity.MYTHIC, mage.cards.t.TeferiTemporalArchmage.class));
        cards.add(new SetCardInfo("Temple of the False God", 314, Rarity.UNCOMMON, mage.cards.t.TempleOfTheFalseGod.class));
        cards.add(new SetCardInfo("Tendrils of Corruption", 166, Rarity.COMMON, mage.cards.t.TendrilsOfCorruption.class));
        cards.add(new SetCardInfo("Terastodon", 218, Rarity.RARE, mage.cards.t.Terastodon.class));
        cards.add(new SetCardInfo("Terramorphic Expanse", 315, Rarity.COMMON, mage.cards.t.TerramorphicExpanse.class));
        cards.add(new SetCardInfo("Thornweald Archer", 219, Rarity.COMMON, mage.cards.t.ThornwealdArcher.class));
        cards.add(new SetCardInfo("Thran Dynamo", 277, Rarity.UNCOMMON, mage.cards.t.ThranDynamo.class));
        cards.add(new SetCardInfo("Thunderfoot Baloth", 49, Rarity.RARE, mage.cards.t.ThunderfootBaloth.class));
        cards.add(new SetCardInfo("Timberwatch Elf", 220, Rarity.COMMON, mage.cards.t.TimberwatchElf.class));
        cards.add(new SetCardInfo("Titania, Protector of Argoth", 50, Rarity.MYTHIC, mage.cards.t.TitaniaProtectorOfArgoth.class));
        cards.add(new SetCardInfo("Titania's Chosen", 221, Rarity.UNCOMMON, mage.cards.t.TitaniasChosen.class));
        cards.add(new SetCardInfo("Tormod's Crypt", 278, Rarity.UNCOMMON, mage.cards.t.TormodsCrypt.class));
        cards.add(new SetCardInfo("Tornado Elemental", 222, Rarity.RARE, mage.cards.t.TornadoElemental.class));
        cards.add(new SetCardInfo("Trading Post", 279, Rarity.RARE, mage.cards.t.TradingPost.class));
        cards.add(new SetCardInfo("Tragic Slip", 167, Rarity.COMMON, mage.cards.t.TragicSlip.class));
        cards.add(new SetCardInfo("Tranquil Thicket", 316, Rarity.COMMON, mage.cards.t.TranquilThicket.class));
        cards.add(new SetCardInfo("True Conviction", 93, Rarity.RARE, mage.cards.t.TrueConviction.class));
        cards.add(new SetCardInfo("Tuktuk the Explorer", 183, Rarity.RARE, mage.cards.t.TuktukTheExplorer.class));
        cards.add(new SetCardInfo("Turn to Frog", 130, Rarity.UNCOMMON, mage.cards.t.TurnToFrog.class));
        cards.add(new SetCardInfo("Twilight Shepherd", 94, Rarity.RARE, mage.cards.t.TwilightShepherd.class));
        cards.add(new SetCardInfo("Tyrant's Familiar", 39, Rarity.RARE, mage.cards.t.TyrantsFamiliar.class));
        cards.add(new SetCardInfo("Unstable Obelisk", 58, Rarity.UNCOMMON, mage.cards.u.UnstableObelisk.class));
        cards.add(new SetCardInfo("Ur-Golem's Eye", 280, Rarity.UNCOMMON, mage.cards.u.UrGolemsEye.class));
        cards.add(new SetCardInfo("Vampire Hexmage", 168, Rarity.UNCOMMON, mage.cards.v.VampireHexmage.class));
        cards.add(new SetCardInfo("Victimize", 169, Rarity.UNCOMMON, mage.cards.v.Victimize.class));
        cards.add(new SetCardInfo("Volcanic Offering", 40, Rarity.RARE, mage.cards.v.VolcanicOffering.class));
        cards.add(new SetCardInfo("Wake the Dead", 31, Rarity.RARE, mage.cards.w.WakeTheDead.class));
        cards.add(new SetCardInfo("Warmonger Hellkite", 41, Rarity.RARE, mage.cards.w.WarmongerHellkite.class));
        cards.add(new SetCardInfo("Wave of Vitriol", 51, Rarity.RARE, mage.cards.w.WaveOfVitriol.class));
        cards.add(new SetCardInfo("Wayfarer's Bauble", 281, Rarity.COMMON, mage.cards.w.WayfarersBauble.class));
        cards.add(new SetCardInfo("Well of Ideas", 20, Rarity.RARE, mage.cards.w.WellOfIdeas.class));
        cards.add(new SetCardInfo("Wellwisher", 223, Rarity.COMMON, mage.cards.w.Wellwisher.class));
        cards.add(new SetCardInfo("Whipflare", 184, Rarity.UNCOMMON, mage.cards.w.Whipflare.class));
        cards.add(new SetCardInfo("Whirlwind", 224, Rarity.RARE, mage.cards.w.Whirlwind.class));
        cards.add(new SetCardInfo("Whitemane Lion", 96, Rarity.COMMON, mage.cards.w.WhitemaneLion.class));
        cards.add(new SetCardInfo("White Sun's Zenith", 95, Rarity.RARE, mage.cards.w.WhiteSunsZenith.class));
        cards.add(new SetCardInfo("Willbender", 131, Rarity.UNCOMMON, mage.cards.w.Willbender.class));
        cards.add(new SetCardInfo("Wing Shards", 97, Rarity.UNCOMMON, mage.cards.w.WingShards.class));
        cards.add(new SetCardInfo("Wolfbriar Elemental", 225, Rarity.RARE, mage.cards.w.WolfbriarElemental.class));
        cards.add(new SetCardInfo("Wolfcaller's Howl", 52, Rarity.RARE, mage.cards.w.WolfcallersHowl.class));
        cards.add(new SetCardInfo("Wood Elves", 226, Rarity.COMMON, mage.cards.w.WoodElves.class));
        cards.add(new SetCardInfo("Word of Seizing", 185, Rarity.RARE, mage.cards.w.WordOfSeizing.class));
        cards.add(new SetCardInfo("Worn Powerstone", 282, Rarity.UNCOMMON, mage.cards.w.WornPowerstone.class));
        cards.add(new SetCardInfo("Wren's Run Packmaster", 227, Rarity.RARE, mage.cards.w.WrensRunPackmaster.class));
        cards.add(new SetCardInfo("Wurmcoil Engine", 283, Rarity.MYTHIC, mage.cards.w.WurmcoilEngine.class));
        cards.add(new SetCardInfo("Xathrid Demon", 170, Rarity.MYTHIC, mage.cards.x.XathridDemon.class));
        cards.add(new SetCardInfo("Zoetic Cavern", 317, Rarity.UNCOMMON, mage.cards.z.ZoeticCavern.class));
    }

}
