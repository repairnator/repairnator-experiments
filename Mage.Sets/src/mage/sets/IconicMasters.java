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
 * @author MajorLazar
 */
public class IconicMasters extends ExpansionSet {

    private static final IconicMasters instance = new IconicMasters();

    public static IconicMasters getInstance() {
        return instance;
    }

    private IconicMasters() {
        super("Iconic Masters", "IMA", ExpansionSet.buildDate(2017, 11, 17), SetType.SUPPLEMENTAL);
        this.blockName = "Reprint";
        this.hasBasicLands = false;
        this.hasBoosters = true;
        this.numBoosterLands = 0;
        this.numBoosterCommon = 11;
        this.numBoosterUncommon = 3;
        this.numBoosterRare = 1;
        this.ratioBoosterMythic = 8;

        cards.add(new SetCardInfo("Scion of Ugin", 1, Rarity.COMMON, mage.cards.s.ScionOfUgin.class));
        cards.add(new SetCardInfo("Abzan Battle Priest", 2, Rarity.UNCOMMON, mage.cards.a.AbzanBattlePriest.class));
        cards.add(new SetCardInfo("Abzan Falconer", 3, Rarity.UNCOMMON, mage.cards.a.AbzanFalconer.class));
        cards.add(new SetCardInfo("Ainok Bond-Kin", 4, Rarity.COMMON, mage.cards.a.AinokBondKin.class));
        cards.add(new SetCardInfo("Ajani's Pridemate", 5, Rarity.UNCOMMON, mage.cards.a.AjanisPridemate.class));
        cards.add(new SetCardInfo("Angel of Mercy", 6, Rarity.COMMON, mage.cards.a.AngelOfMercy.class));
        cards.add(new SetCardInfo("Angelic Accord", 7, Rarity.UNCOMMON, mage.cards.a.AngelicAccord.class));
        cards.add(new SetCardInfo("Archangel of Thune", 8, Rarity.MYTHIC, mage.cards.a.ArchangelOfThune.class));
        cards.add(new SetCardInfo("Auriok Champion", 9, Rarity.RARE, mage.cards.a.AuriokChampion.class));
        cards.add(new SetCardInfo("Austere Command", 10, Rarity.RARE, mage.cards.a.AustereCommand.class));
        cards.add(new SetCardInfo("Avacyn, Angel of Hope", 11, Rarity.MYTHIC, mage.cards.a.AvacynAngelOfHope.class));
        cards.add(new SetCardInfo("Benevolent Ancestor", 12, Rarity.COMMON, mage.cards.b.BenevolentAncestor.class));
        cards.add(new SetCardInfo("Blinding Mage", 13, Rarity.COMMON, mage.cards.b.BlindingMage.class));
        cards.add(new SetCardInfo("Burrenton Forge-Tender", 14, Rarity.UNCOMMON, mage.cards.b.BurrentonForgeTender.class));
        cards.add(new SetCardInfo("Disenchant", 15, Rarity.COMMON, mage.cards.d.Disenchant.class));
        cards.add(new SetCardInfo("Doomed Traveler", 16, Rarity.COMMON, mage.cards.d.DoomedTraveler.class));
        cards.add(new SetCardInfo("Dragon Bell Monk", 17, Rarity.COMMON, mage.cards.d.DragonBellMonk.class));
        cards.add(new SetCardInfo("Elesh Norn, Grand Cenobite", 18, Rarity.MYTHIC, mage.cards.e.EleshNornGrandCenobite.class));
        cards.add(new SetCardInfo("Emerge Unscathed", 19, Rarity.COMMON, mage.cards.e.EmergeUnscathed.class));
        cards.add(new SetCardInfo("Emeria Angel", 20, Rarity.RARE, mage.cards.e.EmeriaAngel.class));
        cards.add(new SetCardInfo("Great Teacher's Decree", 21, Rarity.UNCOMMON, mage.cards.g.GreatTeachersDecree.class));
        cards.add(new SetCardInfo("Guard Duty", 22, Rarity.COMMON, mage.cards.g.GuardDuty.class));
        cards.add(new SetCardInfo("Guided Strike", 23, Rarity.COMMON, mage.cards.g.GuidedStrike.class));
        cards.add(new SetCardInfo("Infantry Veteran", 24, Rarity.COMMON, mage.cards.i.InfantryVeteran.class));
        cards.add(new SetCardInfo("Iona's Judgment", 25, Rarity.COMMON, mage.cards.i.IonasJudgment.class));
        cards.add(new SetCardInfo("Path of Bravery", 26, Rarity.RARE, mage.cards.p.PathOfBravery.class));
        cards.add(new SetCardInfo("Pentarch Ward", 27, Rarity.COMMON, mage.cards.p.PentarchWard.class));
        cards.add(new SetCardInfo("Restoration Angel", 28, Rarity.RARE, mage.cards.r.RestorationAngel.class));
        cards.add(new SetCardInfo("Seeker of the Way", 29, Rarity.COMMON, mage.cards.s.SeekerOfTheWay.class));
        cards.add(new SetCardInfo("Serra Angel", 30, Rarity.UNCOMMON, mage.cards.s.SerraAngel.class));
        cards.add(new SetCardInfo("Serra Ascendant", 31, Rarity.RARE, mage.cards.s.SerraAscendant.class));
        cards.add(new SetCardInfo("Stalwart Aven", 32, Rarity.COMMON, mage.cards.s.StalwartAven.class));
        cards.add(new SetCardInfo("Student of Ojutai", 33, Rarity.COMMON, mage.cards.s.StudentOfOjutai.class));
        cards.add(new SetCardInfo("Survival Cache", 34, Rarity.COMMON, mage.cards.s.SurvivalCache.class));
        cards.add(new SetCardInfo("Sustainer of the Realm", 35, Rarity.COMMON, mage.cards.s.SustainerOfTheRealm.class));
        cards.add(new SetCardInfo("Swords to Plowshares", 36, Rarity.UNCOMMON, mage.cards.s.SwordsToPlowshares.class));
        cards.add(new SetCardInfo("Topan Freeblade", 37, Rarity.UNCOMMON, mage.cards.t.TopanFreeblade.class));
        cards.add(new SetCardInfo("Wing Shards", 38, Rarity.UNCOMMON, mage.cards.w.WingShards.class));
        cards.add(new SetCardInfo("Yosei, the Morning Star", 39, Rarity.RARE, mage.cards.y.YoseiTheMorningStar.class));
        cards.add(new SetCardInfo("Aetherize", 40, Rarity.UNCOMMON, mage.cards.a.Aetherize.class));
        cards.add(new SetCardInfo("Amass the Components", 41, Rarity.COMMON, mage.cards.a.AmassTheComponents.class));
        cards.add(new SetCardInfo("Ancestral Vision", 42, Rarity.RARE, mage.cards.a.AncestralVision.class));
        cards.add(new SetCardInfo("Bewilder", 43, Rarity.COMMON, mage.cards.b.Bewilder.class));
        cards.add(new SetCardInfo("Cephalid Broker", 44, Rarity.UNCOMMON, mage.cards.c.CephalidBroker.class));
        cards.add(new SetCardInfo("Claustrophobia", 45, Rarity.COMMON, mage.cards.c.Claustrophobia.class));
        cards.add(new SetCardInfo("Condescend", 46, Rarity.UNCOMMON, mage.cards.c.Condescend.class));
        cards.add(new SetCardInfo("Consecrated Sphinx", 47, Rarity.MYTHIC, mage.cards.c.ConsecratedSphinx.class));
        cards.add(new SetCardInfo("Cryptic Command", 48, Rarity.RARE, mage.cards.c.CrypticCommand.class));
        cards.add(new SetCardInfo("Day of the Dragons", 49, Rarity.RARE, mage.cards.d.DayOfTheDragons.class));
        cards.add(new SetCardInfo("Diminish", 50, Rarity.COMMON, mage.cards.d.Diminish.class));
        cards.add(new SetCardInfo("Dissolve", 51, Rarity.COMMON, mage.cards.d.Dissolve.class));
        cards.add(new SetCardInfo("Distortion Strike", 52, Rarity.UNCOMMON, mage.cards.d.DistortionStrike.class));
        cards.add(new SetCardInfo("Doorkeeper", 53, Rarity.COMMON, mage.cards.d.Doorkeeper.class));
        cards.add(new SetCardInfo("Elusive Spellfist", 54, Rarity.COMMON, mage.cards.e.ElusiveSpellfist.class));
        cards.add(new SetCardInfo("Flusterstorm", 55, Rarity.RARE, mage.cards.f.Flusterstorm.class));
        cards.add(new SetCardInfo("Fog Bank", 56, Rarity.UNCOMMON, mage.cards.f.FogBank.class));
        cards.add(new SetCardInfo("Frost Lynx", 57, Rarity.COMMON, mage.cards.f.FrostLynx.class));
        cards.add(new SetCardInfo("Illusory Ambusher", 58, Rarity.UNCOMMON, mage.cards.i.IllusoryAmbusher.class));
        cards.add(new SetCardInfo("Illusory Angel", 59, Rarity.UNCOMMON, mage.cards.i.IllusoryAngel.class));
        cards.add(new SetCardInfo("Jace's Phantasm", 60, Rarity.COMMON, mage.cards.j.JacesPhantasm.class));
        cards.add(new SetCardInfo("Jhessian Thief", 61, Rarity.COMMON, mage.cards.j.JhessianThief.class));
        cards.add(new SetCardInfo("Jin-Gitaxias, Core Augur", 62, Rarity.MYTHIC, mage.cards.j.JinGitaxiasCoreAugur.class));
        cards.add(new SetCardInfo("Keiga, the Tide Star", 63, Rarity.RARE, mage.cards.k.KeigaTheTideStar.class));
        cards.add(new SetCardInfo("Mahamoti Djinn", 64, Rarity.UNCOMMON, mage.cards.m.MahamotiDjinn.class));
        cards.add(new SetCardInfo("Mana Drain", 65, Rarity.MYTHIC, mage.cards.m.ManaDrain.class));
        cards.add(new SetCardInfo("Mana Leak", 66, Rarity.COMMON, mage.cards.m.ManaLeak.class));
        cards.add(new SetCardInfo("Mnemonic Wall", 67, Rarity.COMMON, mage.cards.m.MnemonicWall.class));
        cards.add(new SetCardInfo("Ojutai's Breath", 68, Rarity.COMMON, mage.cards.o.OjutaisBreath.class));
        cards.add(new SetCardInfo("Phantom Monster", 69, Rarity.COMMON, mage.cards.p.PhantomMonster.class));
        cards.add(new SetCardInfo("Repeal", 70, Rarity.COMMON, mage.cards.r.Repeal.class));
        cards.add(new SetCardInfo("Riverwheel Aerialists", 71, Rarity.COMMON, mage.cards.r.RiverwheelAerialists.class));
        cards.add(new SetCardInfo("Shriekgeist", 72, Rarity.COMMON, mage.cards.s.Shriekgeist.class));
        cards.add(new SetCardInfo("Skywise Teachings", 73, Rarity.UNCOMMON, mage.cards.s.SkywiseTeachings.class));
        cards.add(new SetCardInfo("Sphinx of Uthuun", 74, Rarity.RARE, mage.cards.s.SphinxOfUthuun.class));
        cards.add(new SetCardInfo("Teferi, Mage of Zhalfir", 75, Rarity.RARE, mage.cards.t.TeferiMageOfZhalfir.class));
        cards.add(new SetCardInfo("Thought Scour", 76, Rarity.COMMON, mage.cards.t.ThoughtScour.class));
        cards.add(new SetCardInfo("Windfall", 77, Rarity.UNCOMMON, mage.cards.w.Windfall.class));
        cards.add(new SetCardInfo("Abyssal Persecutor", 78, Rarity.RARE, mage.cards.a.AbyssalPersecutor.class));
        cards.add(new SetCardInfo("Bala Ged Scorpion", 79, Rarity.COMMON, mage.cards.b.BalaGedScorpion.class));
        cards.add(new SetCardInfo("Balustrade Spy", 80, Rarity.COMMON, mage.cards.b.BalustradeSpy.class));
        cards.add(new SetCardInfo("Bladewing's Thrall", 81, Rarity.UNCOMMON, mage.cards.b.BladewingsThrall.class));
        cards.add(new SetCardInfo("Bloodghast", 82, Rarity.RARE, mage.cards.b.Bloodghast.class));
        cards.add(new SetCardInfo("Bogbrew Witch", 83, Rarity.UNCOMMON, mage.cards.b.BogbrewWitch.class));
        cards.add(new SetCardInfo("Butcher's Glee", 84, Rarity.COMMON, mage.cards.b.ButchersGlee.class));
        cards.add(new SetCardInfo("Child of Night", 85, Rarity.COMMON, mage.cards.c.ChildOfNight.class));
        cards.add(new SetCardInfo("Dead Reveler", 86, Rarity.COMMON, mage.cards.d.DeadReveler.class));
        cards.add(new SetCardInfo("Doom Blade", 87, Rarity.UNCOMMON, mage.cards.d.DoomBlade.class));
        cards.add(new SetCardInfo("Duress", 88, Rarity.COMMON, mage.cards.d.Duress.class));
        cards.add(new SetCardInfo("Eternal Thirst", 89, Rarity.COMMON, mage.cards.e.EternalThirst.class));
        cards.add(new SetCardInfo("Festering Newt", 90, Rarity.COMMON, mage.cards.f.FesteringNewt.class));
        cards.add(new SetCardInfo("Foul-Tongue Invocation", 91, Rarity.COMMON, mage.cards.f.FoulTongueInvocation.class));
        cards.add(new SetCardInfo("Grisly Spectacle", 92, Rarity.COMMON, mage.cards.g.GrislySpectacle.class));
        cards.add(new SetCardInfo("Indulgent Tormentor", 93, Rarity.UNCOMMON, mage.cards.i.IndulgentTormentor.class));
        cards.add(new SetCardInfo("Haunting Hymn", 94, Rarity.UNCOMMON, mage.cards.h.HauntingHymn.class));
        cards.add(new SetCardInfo("Kokusho, the Evening Star", 95, Rarity.RARE, mage.cards.k.KokushoTheEveningStar.class));
        cards.add(new SetCardInfo("Lord of the Pit", 96, Rarity.RARE, mage.cards.l.LordOfThePit.class));
        cards.add(new SetCardInfo("Mer-Ek Nightblade", 97, Rarity.UNCOMMON, mage.cards.m.MerEkNightblade.class));
        cards.add(new SetCardInfo("Necropotence", 98, Rarity.MYTHIC, mage.cards.n.Necropotence.class));
        cards.add(new SetCardInfo("Night of Souls' Betrayal", 99, Rarity.RARE, mage.cards.n.NightOfSoulsBetrayal.class));
        cards.add(new SetCardInfo("Noxious Dragon", 100, Rarity.UNCOMMON, mage.cards.n.NoxiousDragon.class));
        cards.add(new SetCardInfo("Ob Nixilis, the Fallen", 101, Rarity.MYTHIC, mage.cards.o.ObNixilisTheFallen.class));
        cards.add(new SetCardInfo("Phyrexian Rager", 102, Rarity.COMMON, mage.cards.p.PhyrexianRager.class));
        cards.add(new SetCardInfo("Rakdos Drake", 103, Rarity.COMMON, mage.cards.r.RakdosDrake.class));
        cards.add(new SetCardInfo("Reave Soul", 104, Rarity.COMMON, mage.cards.r.ReaveSoul.class));
        cards.add(new SetCardInfo("Rotfeaster Maggot", 105, Rarity.COMMON, mage.cards.r.RotfeasterMaggot.class));
        cards.add(new SetCardInfo("Rune-Scarred Demon", 106, Rarity.RARE, mage.cards.r.RuneScarredDemon.class));
        cards.add(new SetCardInfo("Sanguine Bond", 107, Rarity.UNCOMMON, mage.cards.s.SanguineBond.class));
        cards.add(new SetCardInfo("Sheoldred, Whispering One", 108, Rarity.MYTHIC, mage.cards.s.SheoldredWhisperingOne.class));
        cards.add(new SetCardInfo("Tavern Swindler", 109, Rarity.UNCOMMON, mage.cards.t.TavernSwindler.class));
        cards.add(new SetCardInfo("Thoughtseize", 110, Rarity.RARE, mage.cards.t.Thoughtseize.class));
        cards.add(new SetCardInfo("Thrill-Kill Assassin", 111, Rarity.COMMON, mage.cards.t.ThrillKillAssassin.class));
        cards.add(new SetCardInfo("Virulent Swipe", 112, Rarity.COMMON, mage.cards.v.VirulentSwipe.class));
        cards.add(new SetCardInfo("Wight of Precinct Six", 113, Rarity.COMMON, mage.cards.w.WightOfPrecinctSix.class));
        cards.add(new SetCardInfo("Ulcerate", 114, Rarity.UNCOMMON, mage.cards.u.Ulcerate.class));
        cards.add(new SetCardInfo("Wrench Mind", 115, Rarity.COMMON, mage.cards.w.WrenchMind.class));
        cards.add(new SetCardInfo("Anger of the Gods", 116, Rarity.RARE, mage.cards.a.AngerOfTheGods.class));
        cards.add(new SetCardInfo("Battle-Rattle Shaman", 117, Rarity.COMMON, mage.cards.b.BattleRattleShaman.class));
        cards.add(new SetCardInfo("Bogardan Hellkite", 118, Rarity.RARE, mage.cards.b.BogardanHellkite.class));
        cards.add(new SetCardInfo("Borderland Marauder", 119, Rarity.COMMON, mage.cards.b.BorderlandMarauder.class));
        cards.add(new SetCardInfo("Charmbreaker Devils", 120, Rarity.RARE, mage.cards.c.CharmbreakerDevils.class));
        cards.add(new SetCardInfo("Coordinated Assault", 121, Rarity.UNCOMMON, mage.cards.c.CoordinatedAssault.class));
        cards.add(new SetCardInfo("Crucible of Fire", 122, Rarity.RARE, mage.cards.c.CrucibleOfFire.class));
        cards.add(new SetCardInfo("Draconic Roar", 123, Rarity.COMMON, mage.cards.d.DraconicRoar.class));
        cards.add(new SetCardInfo("Dragon Egg", 124, Rarity.COMMON, mage.cards.d.DragonEgg.class));
        cards.add(new SetCardInfo("Dragon Tempest", 125, Rarity.UNCOMMON, mage.cards.d.DragonTempest.class));
        cards.add(new SetCardInfo("Dragonlord's Servant", 126, Rarity.COMMON, mage.cards.d.DragonlordsServant.class));
        cards.add(new SetCardInfo("Earth Elemental", 127, Rarity.COMMON, mage.cards.e.EarthElemental.class));
        cards.add(new SetCardInfo("Fireball", 128, Rarity.UNCOMMON, mage.cards.f.Fireball.class));
        cards.add(new SetCardInfo("Furnace Whelp", 129, Rarity.COMMON, mage.cards.f.FurnaceWhelp.class));
        cards.add(new SetCardInfo("Fury Charm", 130, Rarity.COMMON, mage.cards.f.FuryCharm.class));
        cards.add(new SetCardInfo("Guttersnipe", 131, Rarity.UNCOMMON, mage.cards.g.Guttersnipe.class));
        cards.add(new SetCardInfo("Hammerhand", 132, Rarity.COMMON, mage.cards.h.Hammerhand.class));
        cards.add(new SetCardInfo("Heat Ray", 133, Rarity.COMMON, mage.cards.h.HeatRay.class));
        cards.add(new SetCardInfo("Hoarding Dragon", 134, Rarity.UNCOMMON, mage.cards.h.HoardingDragon.class));
        cards.add(new SetCardInfo("Keldon Halberdier", 135, Rarity.COMMON, mage.cards.k.KeldonHalberdier.class));
        cards.add(new SetCardInfo("Kiki-Jiki, Mirror Breaker", 136, Rarity.MYTHIC, mage.cards.k.KikiJikiMirrorBreaker.class));
        cards.add(new SetCardInfo("Kiln Fiend", 137, Rarity.COMMON, mage.cards.k.KilnFiend.class));
        cards.add(new SetCardInfo("Mark of Mutiny", 138, Rarity.COMMON, mage.cards.m.MarkOfMutiny.class));
        cards.add(new SetCardInfo("Magus of the Moon", 139, Rarity.RARE, mage.cards.m.MagusOfTheMoon.class));
        cards.add(new SetCardInfo("Monastery Swiftspear", 140, Rarity.UNCOMMON, mage.cards.m.MonasterySwiftspear.class));
        cards.add(new SetCardInfo("Pillar of Flame", 141, Rarity.COMMON, mage.cards.p.PillarOfFlame.class));
        cards.add(new SetCardInfo("Prodigal Pyromancer", 142, Rarity.UNCOMMON, mage.cards.p.ProdigalPyromancer.class));
        cards.add(new SetCardInfo("Rift Bolt", 143, Rarity.UNCOMMON, mage.cards.r.RiftBolt.class));
        cards.add(new SetCardInfo("Ryusei, the Falling Star", 144, Rarity.RARE, mage.cards.r.RyuseiTheFallingStar.class));
        cards.add(new SetCardInfo("Scourge of Valkas", 145, Rarity.RARE, mage.cards.s.ScourgeOfValkas.class));
        cards.add(new SetCardInfo("Splatter Thug", 146, Rarity.COMMON, mage.cards.s.SplatterThug.class));
        cards.add(new SetCardInfo("Staggershock", 147, Rarity.UNCOMMON, mage.cards.s.Staggershock.class));
        cards.add(new SetCardInfo("Surreal Memoir", 148, Rarity.UNCOMMON, mage.cards.s.SurrealMemoir.class));
        cards.add(new SetCardInfo("Thundermaw Hellkite", 149, Rarity.MYTHIC, mage.cards.t.ThundermawHellkite.class));
        cards.add(new SetCardInfo("Tormenting Voice", 150, Rarity.COMMON, mage.cards.t.TormentingVoice.class));
        cards.add(new SetCardInfo("Trumpet Blast", 151, Rarity.COMMON, mage.cards.t.TrumpetBlast.class));
        cards.add(new SetCardInfo("Urabrask the Hidden", 152, Rarity.MYTHIC, mage.cards.u.UrabraskTheHidden.class));
        cards.add(new SetCardInfo("Vent Sentinel", 153, Rarity.COMMON, mage.cards.v.VentSentinel.class));
        cards.add(new SetCardInfo("Aerial Predation", 154, Rarity.COMMON, mage.cards.a.AerialPredation.class));
        cards.add(new SetCardInfo("Assault Formation", 155, Rarity.UNCOMMON, mage.cards.a.AssaultFormation.class));
        cards.add(new SetCardInfo("Carven Caryatid", 156, Rarity.UNCOMMON, mage.cards.c.CarvenCaryatid.class));
        cards.add(new SetCardInfo("Channel", 157, Rarity.MYTHIC, mage.cards.c.Channel.class));
        cards.add(new SetCardInfo("Crowned Ceratok", 158, Rarity.COMMON, mage.cards.c.CrownedCeratok.class));
        cards.add(new SetCardInfo("Curse of Predation", 159, Rarity.RARE, mage.cards.c.CurseOfPredation.class));
        cards.add(new SetCardInfo("Durkwood Baloth", 160, Rarity.COMMON, mage.cards.d.DurkwoodBaloth.class));
        cards.add(new SetCardInfo("Duskdale Wurm", 161, Rarity.COMMON, mage.cards.d.DuskdaleWurm.class));
        cards.add(new SetCardInfo("Enlarge", 162, Rarity.UNCOMMON, mage.cards.e.Enlarge.class));
        cards.add(new SetCardInfo("Genesis Hydra", 163, Rarity.RARE, mage.cards.g.GenesisHydra.class));
        cards.add(new SetCardInfo("Genesis Wave", 164, Rarity.RARE, mage.cards.g.GenesisWave.class));
        cards.add(new SetCardInfo("Greater Basilisk", 165, Rarity.COMMON, mage.cards.g.GreaterBasilisk.class));
        cards.add(new SetCardInfo("Heroes' Bane", 166, Rarity.UNCOMMON, mage.cards.h.HeroesBane.class));
        cards.add(new SetCardInfo("Hunting Pack", 167, Rarity.UNCOMMON, mage.cards.h.HuntingPack.class));
        cards.add(new SetCardInfo("Hunt the Weak", 168, Rarity.COMMON, mage.cards.h.HuntTheWeak.class));
        cards.add(new SetCardInfo("Inspiring Call", 169, Rarity.UNCOMMON, mage.cards.i.InspiringCall.class));
        cards.add(new SetCardInfo("Ivy Elemental", 170, Rarity.COMMON, mage.cards.i.IvyElemental.class));
        cards.add(new SetCardInfo("Jaddi Offshoot", 171, Rarity.COMMON, mage.cards.j.JaddiOffshoot.class));
        cards.add(new SetCardInfo("Jugan, the Rising Star", 172, Rarity.RARE, mage.cards.j.JuganTheRisingStar.class));
        cards.add(new SetCardInfo("Lead the Stampede", 173, Rarity.COMMON, mage.cards.l.LeadTheStampede.class));
        cards.add(new SetCardInfo("Lotus Cobra", 174, Rarity.RARE, mage.cards.l.LotusCobra.class));
        cards.add(new SetCardInfo("Lure", 175, Rarity.UNCOMMON, mage.cards.l.Lure.class));
        cards.add(new SetCardInfo("Nantuko Shaman", 176, Rarity.COMMON, mage.cards.n.NantukoShaman.class));
        cards.add(new SetCardInfo("Nature's Claim", 177, Rarity.COMMON, mage.cards.n.NaturesClaim.class));
        cards.add(new SetCardInfo("Netcaster Spider", 178, Rarity.COMMON, mage.cards.n.NetcasterSpider.class));
        cards.add(new SetCardInfo("Obstinate Baloth", 179, Rarity.RARE, mage.cards.o.ObstinateBaloth.class));
        cards.add(new SetCardInfo("Overgrown Battlement", 180, Rarity.UNCOMMON, mage.cards.o.OvergrownBattlement.class));
        cards.add(new SetCardInfo("Phantom Tiger", 181, Rarity.COMMON, mage.cards.p.PhantomTiger.class));
        cards.add(new SetCardInfo("Prey's Vengeance", 182, Rarity.COMMON, mage.cards.p.PreysVengeance.class));
        cards.add(new SetCardInfo("Primeval Titan", 183, Rarity.MYTHIC, mage.cards.p.PrimevalTitan.class));
        cards.add(new SetCardInfo("Rampaging Baloths", 184, Rarity.RARE, mage.cards.r.RampagingBaloths.class));
        cards.add(new SetCardInfo("Search for Tomorrow", 185, Rarity.COMMON, mage.cards.s.SearchForTomorrow.class));
        cards.add(new SetCardInfo("Sultai Flayer", 186, Rarity.UNCOMMON, mage.cards.s.SultaiFlayer.class));
        cards.add(new SetCardInfo("Timberland Guide", 187, Rarity.COMMON, mage.cards.t.TimberlandGuide.class));
        cards.add(new SetCardInfo("Undercity Troll", 188, Rarity.UNCOMMON, mage.cards.u.UndercityTroll.class));
        cards.add(new SetCardInfo("Vorinclex, Voice of Hunger", 189, Rarity.MYTHIC, mage.cards.v.VorinclexVoiceOfHunger.class));
        cards.add(new SetCardInfo("Wall of Roots", 190, Rarity.COMMON, mage.cards.w.WallOfRoots.class));
        cards.add(new SetCardInfo("Wildsize", 191, Rarity.COMMON, mage.cards.w.Wildsize.class));
        cards.add(new SetCardInfo("Azorius Charm", 192, Rarity.UNCOMMON, mage.cards.a.AzoriusCharm.class));
        cards.add(new SetCardInfo("Bladewing the Risen", 193, Rarity.UNCOMMON, mage.cards.b.BladewingTheRisen.class));
        cards.add(new SetCardInfo("Blizzard Specter", 194, Rarity.UNCOMMON, mage.cards.b.BlizzardSpecter.class));
        cards.add(new SetCardInfo("Blood Baron of Vizkopa", 195, Rarity.RARE, mage.cards.b.BloodBaronOfVizkopa.class));
        cards.add(new SetCardInfo("Chronicler of Heroes", 196, Rarity.UNCOMMON, mage.cards.c.ChroniclerOfHeroes.class));
        cards.add(new SetCardInfo("Corpsejack Menace", 197, Rarity.UNCOMMON, mage.cards.c.CorpsejackMenace.class));
        cards.add(new SetCardInfo("Electrolyze", 198, Rarity.UNCOMMON, mage.cards.e.Electrolyze.class));
        cards.add(new SetCardInfo("Firemane Angel", 199, Rarity.RARE, mage.cards.f.FiremaneAngel.class));
        cards.add(new SetCardInfo("Glimpse the Unthinkable", 200, Rarity.RARE, mage.cards.g.GlimpseTheUnthinkable.class));
        cards.add(new SetCardInfo("Hypersonic Dragon", 201, Rarity.RARE, mage.cards.h.HypersonicDragon.class));
        cards.add(new SetCardInfo("Jungle Barrier", 202, Rarity.UNCOMMON, mage.cards.j.JungleBarrier.class));
        cards.add(new SetCardInfo("Knight of the Reliquary", 203, Rarity.RARE, mage.cards.k.KnightOfTheReliquary.class));
        cards.add(new SetCardInfo("Lightning Helix", 204, Rarity.UNCOMMON, mage.cards.l.LightningHelix.class));
        cards.add(new SetCardInfo("Malfegor", 205, Rarity.RARE, mage.cards.m.Malfegor.class));
        cards.add(new SetCardInfo("Rosheen Meanderer", 206, Rarity.UNCOMMON, mage.cards.r.RosheenMeanderer.class));
        cards.add(new SetCardInfo("Savageborn Hydra", 207, Rarity.RARE, mage.cards.s.SavagebornHydra.class));
        cards.add(new SetCardInfo("Simic Sky Swallower", 208, Rarity.RARE, mage.cards.s.SimicSkySwallower.class));
        cards.add(new SetCardInfo("Spiritmonger", 209, Rarity.RARE, mage.cards.s.Spiritmonger.class));
        cards.add(new SetCardInfo("Supreme Verdict", 210, Rarity.RARE, mage.cards.s.SupremeVerdict.class));
        cards.add(new SetCardInfo("Vizkopa Guildmage", 211, Rarity.UNCOMMON, mage.cards.v.VizkopaGuildmage.class));
        cards.add(new SetCardInfo("Aether Vial", 212, Rarity.RARE, mage.cards.a.AetherVial.class));
        cards.add(new SetCardInfo("Bubbling Cauldron", 213, Rarity.UNCOMMON, mage.cards.b.BubblingCauldron.class));
        cards.add(new SetCardInfo("Darksteel Axe", 214, Rarity.COMMON, mage.cards.d.DarksteelAxe.class));
        cards.add(new SetCardInfo("Dragonloft Idol", 215, Rarity.UNCOMMON, mage.cards.d.DragonloftIdol.class));
        cards.add(new SetCardInfo("Guardian Idol", 216, Rarity.COMMON, mage.cards.g.GuardianIdol.class));
        cards.add(new SetCardInfo("Kolaghan Monument", 217, Rarity.UNCOMMON, mage.cards.k.KolaghanMonument.class));
        cards.add(new SetCardInfo("Manakin", 218, Rarity.COMMON, mage.cards.m.Manakin.class));
        cards.add(new SetCardInfo("Mind Stone", 219, Rarity.COMMON, mage.cards.m.MindStone.class));
        cards.add(new SetCardInfo("Mindcrank", 220, Rarity.UNCOMMON, mage.cards.m.Mindcrank.class));
        cards.add(new SetCardInfo("Mishra's Bauble", 221, Rarity.UNCOMMON, mage.cards.m.MishrasBauble.class));
        cards.add(new SetCardInfo("Moonglove Extract", 222, Rarity.COMMON, mage.cards.m.MoongloveExtract.class));
        cards.add(new SetCardInfo("Oblivion Stone", 223, Rarity.RARE, mage.cards.o.OblivionStone.class));
        cards.add(new SetCardInfo("Palladium Myr", 224, Rarity.UNCOMMON, mage.cards.p.PalladiumMyr.class));
        cards.add(new SetCardInfo("Pristine Talisman", 225, Rarity.UNCOMMON, mage.cards.p.PristineTalisman.class));
        cards.add(new SetCardInfo("Runed Servitor", 226, Rarity.COMMON, mage.cards.r.RunedServitor.class));
        cards.add(new SetCardInfo("Sandstone Oracle", 227, Rarity.UNCOMMON, mage.cards.s.SandstoneOracle.class));
        cards.add(new SetCardInfo("Serum Powder", 228, Rarity.RARE, mage.cards.s.SerumPowder.class));
        cards.add(new SetCardInfo("Star Compass", 229, Rarity.COMMON, mage.cards.s.StarCompass.class));
        cards.add(new SetCardInfo("Thran Dynamo", 230, Rarity.UNCOMMON, mage.cards.t.ThranDynamo.class));
        cards.add(new SetCardInfo("Trepanation Blade", 231, Rarity.UNCOMMON, mage.cards.t.TrepanationBlade.class));
        cards.add(new SetCardInfo("Azorius Chancery", 232, Rarity.UNCOMMON, mage.cards.a.AzoriusChancery.class));
        cards.add(new SetCardInfo("Boros Garrison", 233, Rarity.UNCOMMON, mage.cards.b.BorosGarrison.class));
        cards.add(new SetCardInfo("Dimir Aqueduct", 234, Rarity.UNCOMMON, mage.cards.d.DimirAqueduct.class));
        cards.add(new SetCardInfo("Evolving Wilds", 235, Rarity.COMMON, mage.cards.e.EvolvingWilds.class));
        cards.add(new SetCardInfo("Golgari Rot Farm", 236, Rarity.UNCOMMON, mage.cards.g.GolgariRotFarm.class));
        cards.add(new SetCardInfo("Graven Cairns", 237, Rarity.RARE, mage.cards.g.GravenCairns.class));
        cards.add(new SetCardInfo("Grove of the Burnwillows", 238, Rarity.RARE, mage.cards.g.GroveOfTheBurnwillows.class));
        cards.add(new SetCardInfo("Gruul Turf", 239, Rarity.UNCOMMON, mage.cards.g.GruulTurf.class));
        cards.add(new SetCardInfo("Horizon Canopy", 240, Rarity.RARE, mage.cards.h.HorizonCanopy.class));
        cards.add(new SetCardInfo("Izzet Boilerworks", 241, Rarity.UNCOMMON, mage.cards.i.IzzetBoilerworks.class));
        cards.add(new SetCardInfo("Nimbus Maze", 242, Rarity.RARE, mage.cards.n.NimbusMaze.class));
        cards.add(new SetCardInfo("Orzhov Basilica", 243, Rarity.UNCOMMON, mage.cards.o.OrzhovBasilica.class));
        cards.add(new SetCardInfo("Radiant Fountain", 244, Rarity.COMMON, mage.cards.r.RadiantFountain.class));
        cards.add(new SetCardInfo("Rakdos Carnarium", 245, Rarity.UNCOMMON, mage.cards.r.RakdosCarnarium.class));
        cards.add(new SetCardInfo("River of Tears", 246, Rarity.RARE, mage.cards.r.RiverOfTears.class));
        cards.add(new SetCardInfo("Selesnya Sanctuary", 247, Rarity.UNCOMMON, mage.cards.s.SelesnyaSanctuary.class));
        cards.add(new SetCardInfo("Shimmering Grotto", 248, Rarity.COMMON, mage.cards.s.ShimmeringGrotto.class));
        cards.add(new SetCardInfo("Simic Growth Chamber", 249, Rarity.UNCOMMON, mage.cards.s.SimicGrowthChamber.class));

    }
}
