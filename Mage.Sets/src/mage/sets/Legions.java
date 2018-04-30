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
 * @author North
 */
public class Legions extends ExpansionSet {

    private static final Legions instance = new Legions();

    public static Legions getInstance() {
        return instance;
    }

    private Legions() {
        super("Legions", "LGN", ExpansionSet.buildDate(2003, 1, 25), SetType.EXPANSION);
        this.blockName = "Onslaught";
        this.parentSet = Onslaught.getInstance();
        this.hasBasicLands = false;
        this.hasBoosters = true;
        this.numBoosterLands = 0;
        this.numBoosterCommon = 11;
        this.numBoosterUncommon = 3;
        this.numBoosterRare = 1;
        this.ratioBoosterMythic = 8;
        cards.add(new SetCardInfo("Akroma, Angel of Wrath", 1, Rarity.RARE, mage.cards.a.AkromaAngelOfWrath.class));
        cards.add(new SetCardInfo("Akroma's Devoted", 2, Rarity.UNCOMMON, mage.cards.a.AkromasDevoted.class));
        cards.add(new SetCardInfo("Aphetto Exterminator", 59, Rarity.UNCOMMON, mage.cards.a.AphettoExterminator.class));
        cards.add(new SetCardInfo("Aven Envoy", 30, Rarity.COMMON, mage.cards.a.AvenEnvoy.class));
        cards.add(new SetCardInfo("Aven Redeemer", 3, Rarity.COMMON, mage.cards.a.AvenRedeemer.class));
        cards.add(new SetCardInfo("Aven Warhawk", 4, Rarity.UNCOMMON, mage.cards.a.AvenWarhawk.class));
        cards.add(new SetCardInfo("Bane of the Living", 60, Rarity.RARE, mage.cards.b.BaneOfTheLiving.class));
        cards.add(new SetCardInfo("Beacon of Destiny", 5, Rarity.RARE, mage.cards.b.BeaconOfDestiny.class));
        cards.add(new SetCardInfo("Berserk Murlodont", 117, Rarity.COMMON, mage.cards.b.BerserkMurlodont.class));
        cards.add(new SetCardInfo("Blade Sliver", 88, Rarity.UNCOMMON, mage.cards.b.BladeSliver.class));
        cards.add(new SetCardInfo("Blood Celebrant", 61, Rarity.COMMON, mage.cards.b.BloodCelebrant.class));
        cards.add(new SetCardInfo("Bloodstoke Howler", 89, Rarity.COMMON, mage.cards.b.BloodstokeHowler.class));
        cards.add(new SetCardInfo("Branchsnap Lorian", 118, Rarity.UNCOMMON, mage.cards.b.BranchsnapLorian.class));
        cards.add(new SetCardInfo("Brontotherium", 119, Rarity.UNCOMMON, mage.cards.b.Brontotherium.class));
        cards.add(new SetCardInfo("Brood Sliver", 120, Rarity.RARE, mage.cards.b.BroodSliver.class));
        cards.add(new SetCardInfo("Caller of the Claw", 121, Rarity.RARE, mage.cards.c.CallerOfTheClaw.class));
        cards.add(new SetCardInfo("Canopy Crawler", 122, Rarity.UNCOMMON, mage.cards.c.CanopyCrawler.class));
        cards.add(new SetCardInfo("Celestial Gatekeeper", 6, Rarity.RARE, mage.cards.c.CelestialGatekeeper.class));
        cards.add(new SetCardInfo("Cephalid Pathmage", 31, Rarity.COMMON, mage.cards.c.CephalidPathmage.class));
        cards.add(new SetCardInfo("Chromeshell Crab", 32, Rarity.RARE, mage.cards.c.ChromeshellCrab.class));
        cards.add(new SetCardInfo("Clickslither", 90, Rarity.RARE, mage.cards.c.Clickslither.class));
        cards.add(new SetCardInfo("Cloudreach Cavalry", 7, Rarity.UNCOMMON, mage.cards.c.CloudreachCavalry.class));
        cards.add(new SetCardInfo("Corpse Harvester", 62, Rarity.UNCOMMON, mage.cards.c.CorpseHarvester.class));
        cards.add(new SetCardInfo("Covert Operative", 33, Rarity.COMMON, mage.cards.c.CovertOperative.class));
        cards.add(new SetCardInfo("Crested Craghorn", 91, Rarity.COMMON, mage.cards.c.CrestedCraghorn.class));
        cards.add(new SetCardInfo("Crookclaw Elder", 34, Rarity.UNCOMMON, mage.cards.c.CrookclawElder.class));
        cards.add(new SetCardInfo("Crypt Sliver", 63, Rarity.COMMON, mage.cards.c.CryptSliver.class));
        cards.add(new SetCardInfo("Dark Supplicant", 64, Rarity.UNCOMMON, mage.cards.d.DarkSupplicant.class));
        cards.add(new SetCardInfo("Daru Mender", 8, Rarity.UNCOMMON, mage.cards.d.DaruMender.class));
        cards.add(new SetCardInfo("Daru Sanctifier", 9, Rarity.COMMON, mage.cards.d.DaruSanctifier.class));
        cards.add(new SetCardInfo("Daru Stinger", 10, Rarity.COMMON, mage.cards.d.DaruStinger.class));
        cards.add(new SetCardInfo("Deathmark Prelate", 65, Rarity.UNCOMMON, mage.cards.d.DeathmarkPrelate.class));
        cards.add(new SetCardInfo("Defender of the Order", 11, Rarity.RARE, mage.cards.d.DefenderOfTheOrder.class));
        cards.add(new SetCardInfo("Defiant Elf", 123, Rarity.COMMON, mage.cards.d.DefiantElf.class));
        cards.add(new SetCardInfo("Deftblade Elite", 12, Rarity.COMMON, mage.cards.d.DeftbladeElite.class));
        cards.add(new SetCardInfo("Dermoplasm", 35, Rarity.RARE, mage.cards.d.Dermoplasm.class));
        cards.add(new SetCardInfo("Dreamborn Muse", 36, Rarity.RARE, mage.cards.d.DreambornMuse.class));
        cards.add(new SetCardInfo("Drinker of Sorrow", 66, Rarity.RARE, mage.cards.d.DrinkerOfSorrow.class));
        cards.add(new SetCardInfo("Dripping Dead", 67, Rarity.COMMON, mage.cards.d.DrippingDead.class));
        cards.add(new SetCardInfo("Earthblighter", 68, Rarity.UNCOMMON, mage.cards.e.Earthblighter.class));
        cards.add(new SetCardInfo("Echo Tracer", 37, Rarity.COMMON, mage.cards.e.EchoTracer.class));
        cards.add(new SetCardInfo("Elvish Soultiller", 124, Rarity.RARE, mage.cards.e.ElvishSoultiller.class));
        cards.add(new SetCardInfo("Embalmed Brawler", 69, Rarity.COMMON, mage.cards.e.EmbalmedBrawler.class));
        cards.add(new SetCardInfo("Enormous Baloth", 125, Rarity.UNCOMMON, mage.cards.e.EnormousBaloth.class));
        cards.add(new SetCardInfo("Essence Sliver", 13, Rarity.RARE, mage.cards.e.EssenceSliver.class));
        cards.add(new SetCardInfo("Feral Throwback", 126, Rarity.RARE, mage.cards.f.FeralThrowback.class));
        cards.add(new SetCardInfo("Flamewave Invoker", 92, Rarity.COMMON, mage.cards.f.FlamewaveInvoker.class));
        cards.add(new SetCardInfo("Frenetic Raptor", 93, Rarity.UNCOMMON, mage.cards.f.FreneticRaptor.class));
        cards.add(new SetCardInfo("Fugitive Wizard", 38, Rarity.COMMON, mage.cards.f.FugitiveWizard.class));
        cards.add(new SetCardInfo("Gempalm Avenger", 14, Rarity.COMMON, mage.cards.g.GempalmAvenger.class));
        cards.add(new SetCardInfo("Gempalm Incinerator", 94, Rarity.UNCOMMON, mage.cards.g.GempalmIncinerator.class));
        cards.add(new SetCardInfo("Gempalm Polluter", 70, Rarity.COMMON, mage.cards.g.GempalmPolluter.class));
        cards.add(new SetCardInfo("Gempalm Sorcerer", 39, Rarity.UNCOMMON, mage.cards.g.GempalmSorcerer.class));
        cards.add(new SetCardInfo("Gempalm Strider", 127, Rarity.UNCOMMON, mage.cards.g.GempalmStrider.class));
        cards.add(new SetCardInfo("Ghastly Remains", 71, Rarity.RARE, mage.cards.g.GhastlyRemains.class));
        cards.add(new SetCardInfo("Glintwing Invoker", 40, Rarity.COMMON, mage.cards.g.GlintwingInvoker.class));
        cards.add(new SetCardInfo("Glowering Rogon", 128, Rarity.COMMON, mage.cards.g.GloweringRogon.class));
        cards.add(new SetCardInfo("Glowrider", 15, Rarity.RARE, mage.cards.g.Glowrider.class));
        cards.add(new SetCardInfo("Goblin Assassin", 95, Rarity.UNCOMMON, mage.cards.g.GoblinAssassin.class));
        cards.add(new SetCardInfo("Goblin Clearcutter", 96, Rarity.UNCOMMON, mage.cards.g.GoblinClearcutter.class));
        cards.add(new SetCardInfo("Goblin Dynamo", 97, Rarity.UNCOMMON, mage.cards.g.GoblinDynamo.class));
        cards.add(new SetCardInfo("Goblin Firebug", 98, Rarity.COMMON, mage.cards.g.GoblinFirebug.class));
        cards.add(new SetCardInfo("Goblin Goon", 99, Rarity.RARE, mage.cards.g.GoblinGoon.class));
        cards.add(new SetCardInfo("Goblin Grappler", 100, Rarity.COMMON, mage.cards.g.GoblinGrappler.class));
        cards.add(new SetCardInfo("Goblin Lookout", 101, Rarity.COMMON, mage.cards.g.GoblinLookout.class));
        cards.add(new SetCardInfo("Goblin Turncoat", 72, Rarity.COMMON, mage.cards.g.GoblinTurncoat.class));
        cards.add(new SetCardInfo("Graveborn Muse", 73, Rarity.RARE, mage.cards.g.GravebornMuse.class));
        cards.add(new SetCardInfo("Havoc Demon", 74, Rarity.RARE, mage.cards.h.HavocDemon.class));
        cards.add(new SetCardInfo("Hollow Specter", 75, Rarity.RARE, mage.cards.h.HollowSpecter.class));
        cards.add(new SetCardInfo("Hundroog", 129, Rarity.COMMON, mage.cards.h.Hundroog.class));
        cards.add(new SetCardInfo("Hunter Sliver", 102, Rarity.COMMON, mage.cards.h.HunterSliver.class));
        cards.add(new SetCardInfo("Imperial Hellkite", 103, Rarity.RARE, mage.cards.i.ImperialHellkite.class));
        cards.add(new SetCardInfo("Infernal Caretaker", 76, Rarity.COMMON, mage.cards.i.InfernalCaretaker.class));
        cards.add(new SetCardInfo("Keeneye Aven", 41, Rarity.COMMON, mage.cards.k.KeeneyeAven.class));
        cards.add(new SetCardInfo("Keeper of the Nine Gales", 42, Rarity.RARE, mage.cards.k.KeeperOfTheNineGales.class));
        cards.add(new SetCardInfo("Kilnmouth Dragon", 104, Rarity.RARE, mage.cards.k.KilnmouthDragon.class));
        cards.add(new SetCardInfo("Krosan Cloudscraper", 130, Rarity.RARE, mage.cards.k.KrosanCloudscraper.class));
        cards.add(new SetCardInfo("Krosan Vorine", 131, Rarity.COMMON, mage.cards.k.KrosanVorine.class));
        cards.add(new SetCardInfo("Lavaborn Muse", 105, Rarity.RARE, mage.cards.l.LavabornMuse.class));
        cards.add(new SetCardInfo("Liege of the Axe", 16, Rarity.UNCOMMON, mage.cards.l.LiegeOfTheAxe.class));
        cards.add(new SetCardInfo("Lowland Tracker", 17, Rarity.COMMON, mage.cards.l.LowlandTracker.class));
        cards.add(new SetCardInfo("Macetail Hystrodon", 106, Rarity.COMMON, mage.cards.m.MacetailHystrodon.class));
        cards.add(new SetCardInfo("Magma Sliver", 107, Rarity.RARE, mage.cards.m.MagmaSliver.class));
        cards.add(new SetCardInfo("Master of the Veil", 43, Rarity.UNCOMMON, mage.cards.m.MasterOfTheVeil.class));
        cards.add(new SetCardInfo("Merchant of Secrets", 44, Rarity.COMMON, mage.cards.m.MerchantOfSecrets.class));
        cards.add(new SetCardInfo("Mistform Seaswift", 45, Rarity.COMMON, mage.cards.m.MistformSeaswift.class));
        cards.add(new SetCardInfo("Mistform Sliver", 46, Rarity.COMMON, mage.cards.m.MistformSliver.class));
        cards.add(new SetCardInfo("Mistform Ultimus", 47, Rarity.RARE, mage.cards.m.MistformUltimus.class));
        cards.add(new SetCardInfo("Mistform Wakecaster", 48, Rarity.UNCOMMON, mage.cards.m.MistformWakecaster.class));
        cards.add(new SetCardInfo("Nantuko Vigilante", 132, Rarity.COMMON, mage.cards.n.NantukoVigilante.class));
        cards.add(new SetCardInfo("Needleshot Gourna", 133, Rarity.COMMON, mage.cards.n.NeedleshotGourna.class));
        cards.add(new SetCardInfo("Noxious Ghoul", 77, Rarity.UNCOMMON, mage.cards.n.NoxiousGhoul.class));
        cards.add(new SetCardInfo("Patron of the Wild", 134, Rarity.COMMON, mage.cards.p.PatronOfTheWild.class));
        cards.add(new SetCardInfo("Phage the Untouchable", 78, Rarity.RARE, mage.cards.p.PhageTheUntouchable.class));
        cards.add(new SetCardInfo("Planar Guide", 18, Rarity.RARE, mage.cards.p.PlanarGuide.class));
        cards.add(new SetCardInfo("Plated Sliver", 19, Rarity.COMMON, mage.cards.p.PlatedSliver.class));
        cards.add(new SetCardInfo("Primal Whisperer", 135, Rarity.RARE, mage.cards.p.PrimalWhisperer.class));
        cards.add(new SetCardInfo("Primoc Escapee", 49, Rarity.UNCOMMON, mage.cards.p.PrimocEscapee.class));
        cards.add(new SetCardInfo("Quick Sliver", 136, Rarity.COMMON, mage.cards.q.QuickSliver.class));
        cards.add(new SetCardInfo("Ridgetop Raptor", 108, Rarity.UNCOMMON, mage.cards.r.RidgetopRaptor.class));
        cards.add(new SetCardInfo("Riptide Director", 50, Rarity.RARE, mage.cards.r.RiptideDirector.class));
        cards.add(new SetCardInfo("Riptide Mangler", 51, Rarity.RARE, mage.cards.r.RiptideMangler.class));
        cards.add(new SetCardInfo("Rockshard Elemental", 109, Rarity.RARE, mage.cards.r.RockshardElemental.class));
        cards.add(new SetCardInfo("Root Sliver", 137, Rarity.UNCOMMON, mage.cards.r.RootSliver.class));
        cards.add(new SetCardInfo("Scion of Darkness", 79, Rarity.RARE, mage.cards.s.ScionOfDarkness.class));
        cards.add(new SetCardInfo("Seedborn Muse", 138, Rarity.RARE, mage.cards.s.SeedbornMuse.class));
        cards.add(new SetCardInfo("Shaleskin Plower", 110, Rarity.COMMON, mage.cards.s.ShaleskinPlower.class));
        cards.add(new SetCardInfo("Shifting Sliver", 52, Rarity.UNCOMMON, mage.cards.s.ShiftingSliver.class));
        cards.add(new SetCardInfo("Skinthinner", 80, Rarity.COMMON, mage.cards.s.Skinthinner.class));
        cards.add(new SetCardInfo("Skirk Alarmist", 111, Rarity.RARE, mage.cards.s.SkirkAlarmist.class));
        cards.add(new SetCardInfo("Skirk Drill Sergeant", 112, Rarity.UNCOMMON, mage.cards.s.SkirkDrillSergeant.class));
        cards.add(new SetCardInfo("Skirk Marauder", 113, Rarity.COMMON, mage.cards.s.SkirkMarauder.class));
        cards.add(new SetCardInfo("Skirk Outrider", 114, Rarity.COMMON, mage.cards.s.SkirkOutrider.class));
        cards.add(new SetCardInfo("Smokespew Invoker", 81, Rarity.COMMON, mage.cards.s.SmokespewInvoker.class));
        cards.add(new SetCardInfo("Sootfeather Flock", 82, Rarity.COMMON, mage.cards.s.SootfeatherFlock.class));
        cards.add(new SetCardInfo("Spectral Sliver", 83, Rarity.UNCOMMON, mage.cards.s.SpectralSliver.class));
        cards.add(new SetCardInfo("Starlight Invoker", 20, Rarity.COMMON, mage.cards.s.StarlightInvoker.class));
        cards.add(new SetCardInfo("Stoic Champion", 21, Rarity.UNCOMMON, mage.cards.s.StoicChampion.class));
        cards.add(new SetCardInfo("Stonewood Invoker", 139, Rarity.COMMON, mage.cards.s.StonewoodInvoker.class));
        cards.add(new SetCardInfo("Sunstrike Legionnaire", 22, Rarity.RARE, mage.cards.s.SunstrikeLegionnaire.class));
        cards.add(new SetCardInfo("Swooping Talon", 23, Rarity.UNCOMMON, mage.cards.s.SwoopingTalon.class));
        cards.add(new SetCardInfo("Synapse Sliver", 53, Rarity.RARE, mage.cards.s.SynapseSliver.class));
        cards.add(new SetCardInfo("Timberwatch Elf", 140, Rarity.COMMON, mage.cards.t.TimberwatchElf.class));
        cards.add(new SetCardInfo("Totem Speaker", 141, Rarity.UNCOMMON, mage.cards.t.TotemSpeaker.class));
        cards.add(new SetCardInfo("Toxin Sliver", 84, Rarity.RARE, mage.cards.t.ToxinSliver.class));
        cards.add(new SetCardInfo("Tribal Forcemage", 142, Rarity.RARE, mage.cards.t.TribalForcemage.class));
        cards.add(new SetCardInfo("Unstable Hulk", 115, Rarity.RARE, mage.cards.u.UnstableHulk.class));
        cards.add(new SetCardInfo("Vexing Beetle", 143, Rarity.RARE, mage.cards.v.VexingBeetle.class));
        cards.add(new SetCardInfo("Vile Deacon", 85, Rarity.COMMON, mage.cards.v.VileDeacon.class));
        cards.add(new SetCardInfo("Voidmage Apprentice", 54, Rarity.COMMON, mage.cards.v.VoidmageApprentice.class));
        cards.add(new SetCardInfo("Wall of Deceit", 55, Rarity.UNCOMMON, mage.cards.w.WallOfDeceit.class));
        cards.add(new SetCardInfo("Wall of Hope", 24, Rarity.COMMON, mage.cards.w.WallOfHope.class));
        cards.add(new SetCardInfo("Warbreak Trumpeter", 116, Rarity.UNCOMMON, mage.cards.w.WarbreakTrumpeter.class));
        cards.add(new SetCardInfo("Ward Sliver", 25, Rarity.UNCOMMON, mage.cards.w.WardSliver.class));
        cards.add(new SetCardInfo("Warped Researcher", 56, Rarity.UNCOMMON, mage.cards.w.WarpedResearcher.class));
        cards.add(new SetCardInfo("Weaver of Lies", 57, Rarity.RARE, mage.cards.w.WeaverOfLies.class));
        cards.add(new SetCardInfo("Whipgrass Entangler", 26, Rarity.COMMON, mage.cards.w.WhipgrassEntangler.class));
        cards.add(new SetCardInfo("White Knight", 27, Rarity.UNCOMMON, mage.cards.w.WhiteKnight.class));
        cards.add(new SetCardInfo("Willbender", 58, Rarity.UNCOMMON, mage.cards.w.Willbender.class));
        cards.add(new SetCardInfo("Windborn Muse", 28, Rarity.RARE, mage.cards.w.WindbornMuse.class));
        cards.add(new SetCardInfo("Wingbeat Warrior", 29, Rarity.COMMON, mage.cards.w.WingbeatWarrior.class));
        cards.add(new SetCardInfo("Wirewood Channeler", 144, Rarity.UNCOMMON, mage.cards.w.WirewoodChanneler.class));
        cards.add(new SetCardInfo("Wirewood Hivemaster", 145, Rarity.UNCOMMON, mage.cards.w.WirewoodHivemaster.class));
        cards.add(new SetCardInfo("Withered Wretch", 86, Rarity.UNCOMMON, mage.cards.w.WitheredWretch.class));
        cards.add(new SetCardInfo("Zombie Brute", 87, Rarity.UNCOMMON, mage.cards.z.ZombieBrute.class));
    }
}
