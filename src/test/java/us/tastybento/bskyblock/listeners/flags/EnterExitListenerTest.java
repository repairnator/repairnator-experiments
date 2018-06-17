/**
 * 
 */
package us.tastybento.bskyblock.listeners.flags;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import us.tastybento.bskyblock.BSkyBlock;
import us.tastybento.bskyblock.Settings;
import us.tastybento.bskyblock.api.configuration.WorldSettings;
import us.tastybento.bskyblock.api.user.Notifier;
import us.tastybento.bskyblock.api.user.User;
import us.tastybento.bskyblock.database.objects.Island;
import us.tastybento.bskyblock.managers.IslandWorldManager;
import us.tastybento.bskyblock.managers.IslandsManager;
import us.tastybento.bskyblock.managers.LocalesManager;
import us.tastybento.bskyblock.managers.PlayersManager;
import us.tastybento.bskyblock.util.Util;

/**
 * @author tastybento
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Bukkit.class, BSkyBlock.class, Util.class })
public class EnterExitListenerTest {

    private static final Integer PROTECTION_RANGE = 200;
    private static final Integer X = 600;
    private static final Integer Y = 120;
    private static final Integer Z = 10000;
    private UUID uuid;
    private User user;
    private IslandsManager im;
    private Island island;
    private World world;
    private Location outside;
    private Location inside;
    private Notifier notifier;
    private Location inside2;
    private EnterExitListener listener;
    private LocalesManager lm;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

        // Set up plugin
        BSkyBlock plugin = mock(BSkyBlock.class);
        Whitebox.setInternalState(BSkyBlock.class, "instance", plugin);

        // World
        world = mock(World.class);

        // Settings
        Settings s = mock(Settings.class);
        when(plugin.getSettings()).thenReturn(s);

        // Player
        Player p = mock(Player.class);
        // Sometimes use Mockito.withSettings().verboseLogging()
        user = mock(User.class);
        User.setPlugin(plugin);
        when(user.isOp()).thenReturn(false);
        uuid = UUID.randomUUID();
        when(user.getUniqueId()).thenReturn(uuid);
        when(user.getPlayer()).thenReturn(p);
        when(user.getName()).thenReturn("tastybento");

        // No island for player to begin with (set it later in the tests)
        im = mock(IslandsManager.class);
        when(plugin.getIslands()).thenReturn(im);

        // Locales      
        lm = mock(LocalesManager.class);
        when(plugin.getLocalesManager()).thenReturn(lm);
        when(lm.get(any(), any())).thenReturn("mock translation");

        // Notifier
        notifier = mock(Notifier.class);
        when(plugin.getNotifier()).thenReturn(notifier);

        // Island initialization
        island = mock(Island.class);
        Location loc = mock(Location.class);
        when(loc.getWorld()).thenReturn(world);
        when(loc.getBlockX()).thenReturn(X);
        when(loc.getBlockY()).thenReturn(Y);
        when(loc.getBlockZ()).thenReturn(Z);
        when(island.getCenter()).thenReturn(loc);
        when(island.getProtectionRange()).thenReturn(PROTECTION_RANGE);
        when(island.getOwner()).thenReturn(uuid);

        when(im.getIsland(Mockito.any(), Mockito.any(UUID.class))).thenReturn(island);

        // Common from to's
        outside = mock(Location.class);
        when(outside.getWorld()).thenReturn(world);
        when(outside.getBlockX()).thenReturn(X + PROTECTION_RANGE + 1);
        when(outside.getBlockY()).thenReturn(Y);
        when(outside.getBlockZ()).thenReturn(Z);
        when(outside.toVector()).thenReturn(new Vector(X + PROTECTION_RANGE + 1, Y, Z));

        inside = mock(Location.class);
        when(inside.getWorld()).thenReturn(world);
        when(inside.getBlockX()).thenReturn(X + PROTECTION_RANGE - 1);
        when(inside.getBlockY()).thenReturn(Y);
        when(inside.getBlockZ()).thenReturn(Z);
        when(inside.toVector()).thenReturn(new Vector(X + PROTECTION_RANGE - 1, Y, Z));

        inside2 = mock(Location.class);
        when(inside.getWorld()).thenReturn(world);
        when(inside.getBlockX()).thenReturn(X + PROTECTION_RANGE - 2);
        when(inside.getBlockY()).thenReturn(Y);
        when(inside.getBlockZ()).thenReturn(Z);
        when(inside.toVector()).thenReturn(new Vector(X + PROTECTION_RANGE -2, Y, Z));

        Optional<Island> opIsland = Optional.ofNullable(island);
        when(im.getProtectedIslandAt(Mockito.eq(inside))).thenReturn(opIsland);
        when(im.getProtectedIslandAt(Mockito.eq(inside2))).thenReturn(opIsland);
        when(im.getProtectedIslandAt(Mockito.eq(outside))).thenReturn(Optional.empty());
        
        // Island World Manager
        IslandWorldManager iwm = mock(IslandWorldManager.class);
        when(iwm.inWorld(Mockito.any())).thenReturn(true);
        when(plugin.getIWM()).thenReturn(iwm);
        
        // Player's manager
        PlayersManager pm = mock(PlayersManager.class);
        when(pm.getName(Mockito.any())).thenReturn("tastybento");
        when(plugin.getPlayers()).thenReturn(pm);

        // Listener
        listener = new EnterExitListener();

        PowerMockito.mockStatic(Util.class);
        when(Util.getWorld(Mockito.any())).thenReturn(world);
        
        // World Settings
        WorldSettings ws = mock(WorldSettings.class);
        when(iwm.getWorldSettings(Mockito.any())).thenReturn(ws);
        Map<String, Boolean> worldFlags = new HashMap<>();
        when(ws.getWorldFlags()).thenReturn(worldFlags);
    }

    /**
     * Test method for {@link us.tastybento.bskyblock.listeners.flags.EnterExitListener#onMove(org.bukkit.event.player.PlayerMoveEvent)}.
     */
    @Test
    public void testOnMoveInsideIsland() {        
        PlayerMoveEvent e = new PlayerMoveEvent(user.getPlayer(), inside, inside);
        listener.onMove(e);
        // Moving in the island should result in no messages to the user
        Mockito.verify(user, Mockito.never()).sendMessage(Mockito.anyVararg());
    }

    /**
     * Test method for {@link us.tastybento.bskyblock.listeners.flags.EnterExitListener#onMove(org.bukkit.event.player.PlayerMoveEvent)}.
     */
    @Test
    public void testOnMoveOutsideIsland() {        
        PlayerMoveEvent e = new PlayerMoveEvent(user.getPlayer(), outside, outside);
        listener.onMove(e);
        // Moving outside the island should result in no messages to the user
        Mockito.verify(user, Mockito.never()).sendMessage(Mockito.anyVararg());
    }

    /**
     * Test method for {@link us.tastybento.bskyblock.listeners.flags.EnterExitListener#onMove(org.bukkit.event.player.PlayerMoveEvent)}.
     */
    @Test
    public void testOnGoingIntoIslandEmptyIslandName() {
        when(island.getName()).thenReturn("");
        PlayerMoveEvent e = new PlayerMoveEvent(user.getPlayer(), outside, inside);
        listener.onMove(e);
        // Moving into the island should show a message
        Mockito.verify(lm).get(Mockito.any(), Mockito.eq("protection.flags.ENTER_EXIT_MESSAGES.now-entering"));
        // The island owner needs to be checked
        Mockito.verify(island, Mockito.times(3)).getOwner();
    }
    
    /**
     * Test method for {@link us.tastybento.bskyblock.listeners.flags.EnterExitListener#onMove(org.bukkit.event.player.PlayerMoveEvent)}.
     */
    @Test
    public void testOnGoingIntoIslandWithIslandName() {
        when(island.getName()).thenReturn("fancy name");
        PlayerMoveEvent e = new PlayerMoveEvent(user.getPlayer(), outside, inside);
        listener.onMove(e);
        // Moving into the island should show a message
        Mockito.verify(lm).get(Mockito.any(), Mockito.eq("protection.flags.ENTER_EXIT_MESSAGES.now-entering"));
        // No owner check
        Mockito.verify(island, Mockito.times(2)).getOwner();
        Mockito.verify(island, Mockito.times(2)).getName();
    }

    /**
     * Test method for {@link us.tastybento.bskyblock.listeners.flags.EnterExitListener#onMove(org.bukkit.event.player.PlayerMoveEvent)}.
     */
    @Test
    public void testExitingIslandEmptyIslandName() {
        when(island.getName()).thenReturn("");
        PlayerMoveEvent e = new PlayerMoveEvent(user.getPlayer(), inside, outside);
        listener.onMove(e);
        // Moving into the island should show a message
        Mockito.verify(lm).get(Mockito.any(), Mockito.eq("protection.flags.ENTER_EXIT_MESSAGES.now-leaving"));
        // The island owner needs to be checked
        Mockito.verify(island, Mockito.times(3)).getOwner();
    }
    
    /**
     * Test method for {@link us.tastybento.bskyblock.listeners.flags.EnterExitListener#onMove(org.bukkit.event.player.PlayerMoveEvent)}.
     */
    @Test
    public void testExitingIslandWithIslandName() {
        when(island.getName()).thenReturn("fancy name");
        PlayerMoveEvent e = new PlayerMoveEvent(user.getPlayer(), inside, outside);
        listener.onMove(e);
        // Moving into the island should show a message
        Mockito.verify(lm).get(Mockito.any(), Mockito.eq("protection.flags.ENTER_EXIT_MESSAGES.now-leaving"));
        // No owner check
        Mockito.verify(island, Mockito.times(2)).getOwner();
        Mockito.verify(island, Mockito.times(2)).getName();
    }

}
