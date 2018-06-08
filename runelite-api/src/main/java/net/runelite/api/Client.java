/*
 * Copyright (c) 2016-2017, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.api;

import java.awt.Canvas;
import java.awt.Dimension;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.runelite.api.annotations.VisibleForDevtools;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.vars.AccountType;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

/**
 * Represents the RuneScape client.
 */
public interface Client extends GameEngine
{
	/**
	 * Gets a list of all valid players from the player cache.
	 *
	 * @return a list of all players
	 */
	List<Player> getPlayers();

	/**
	 * Gets a list of all valid NPCs from the NPC cache.
	 *
	 * @return a list of all NPCs
	 */
	List<NPC> getNpcs();

	/**
	 * Gets an array of all cached NPCs.
	 *
	 * @return cached NPCs
	 */
	NPC[] getCachedNPCs();

	/**
	 * Gets an array of all cached players.
	 *
	 * @return cached players
	 */
	Player[] getCachedPlayers();

	/**
	 * Gets the current modified level of a skill.
	 *
	 * @param skill the skill
	 * @return the modified skill level
	 */
	int getBoostedSkillLevel(Skill skill);

	/**
	 * Gets the real level of a skill.
	 *
	 * @param skill the skill
	 * @return the skill level
	 */
	int getRealSkillLevel(Skill skill);

	/**
	 * Adds a new chat message to the chatbox.
	 *
	 * @param type the type of message
	 * @param name the name of the player that sent the message
	 * @param message the message contents
	 * @param sender the sender/channel name
	 */
	void addChatMessage(ChatMessageType type, String name, String message, String sender);

	/**
	 * Gets the current game state.
	 *
	 * @return the game state
	 */
	GameState getGameState();

	/**
	 * Gets the current logged in username.
	 *
	 * @return the logged in username
	 */
	String getUsername();

	/**
	 * Sets the current logged in username.
	 *
	 * @param name the logged in username
	 */
	void setUsername(String name);

	/**
	 * Gets the account type of the logged in player.
	 *
	 * @return the account type
	 */
	AccountType getAccountType();

	@Override
	Canvas getCanvas();

	/**
	 * Gets the current FPS (frames per second).
	 *
	 * @return the FPS
	 */
	int getFPS();

	/**
	 * Gets the x-axis coordinate of the camera.
	 * <p>
	 * This value is a local coordinate value similar to
	 * {@link #getLocalDestinationLocation()}.
	 *
	 * @return the camera x coordinate
	 */
	int getCameraX();

	/**
	 * Gets the y-axis coordinate of the camera.
	 * <p>
	 * This value is a local coordinate value similar to
	 * {@link #getLocalDestinationLocation()}.
	 *
	 * @return the camera y coordinate
	 */
	int getCameraY();

	/**
	 * Gets the z-axis coordinate of the camera.
	 * <p>
	 * This value is a local coordinate value similar to
	 * {@link #getLocalDestinationLocation()}.
	 *
	 * @return the camera z coordinate
	 */
	int getCameraZ();

	/**
	 * Gets the actual pitch of the camera.
	 * <p>
	 * The value returned by this method is measured in JAU, or Jagex
	 * Angle Unit, which is 1/1024 of a revolution.
	 *
	 * @return the camera pitch
	 */
	int getCameraPitch();

	/**
	 * Gets the yaw of the camera.
	 *
	 * @return the camera yaw
	 */
	int getCameraYaw();

	/**
	 * Gets the current world number of the logged in player.
	 *
	 * @return the logged in world number
	 */
	int getWorld();

	/**
	 * Gets the height of the viewport.
	 *
	 * @return the viewport height
	 */
	int getViewportHeight();

	/**
	 * Gets the width of the viewport.
	 *
	 * @return the viewport width
	 */
	int getViewportWidth();

	/**
	 * Gets the x-axis offset of the viewport.
	 *
	 * @return the x-axis offset
	 */
	int getViewportXOffset();

	/**
	 * Gets the y-axis offset of the viewport.
	 *
	 * @return the y-axis offset
	 */
	int getViewportYOffset();

	/**
	 * Gets the scale of the world (zoom value).
	 *
	 * @return the world scale
	 */
	int getScale();

	/**
	 * Gets the current position of the mouse on the canvas.
	 *
	 * @return the mouse canvas position
	 */
	Point getMouseCanvasPosition();

	/**
	 * Gets a 3D array containing the heights of tiles in the
	 * current scene.
	 *
	 * @return the tile heights
	 */
	int[][][] getTileHeights();

	/**
	 * Gets a 3D array containing the settings of tiles in the
	 * current scene.
	 *
	 * @return the tile settings
	 */
	byte[][][] getTileSettings();

	/**
	 * Gets the current plane the player is on.
	 * <p>
	 * This value indicates the current map level above ground level, where
	 * ground level is 0. For example, going up a ladder in Lumbridge castle
	 * will put the player on plane 1.
	 * <p>
	 * Note: This value will never be below 0. Basements and caves below ground
	 * level use a tile offset and are still considered plane 0 by the game.
	 *
	 * @return the plane
	 */
	int getPlane();

	/**
	 * Gets the current region the local player is in.
	 *
	 * @return the region
	 */
	Region getRegion();

	/**
	 * Gets the logged in player instance.
	 *
	 * @return the logged in player
	 */
	Player getLocalPlayer();

	/**
	 * Gets the item composition corresponding to an items ID.
	 *
	 * @param id the item ID
	 * @return the corresponding item composition
	 * @see ItemID
	 */
	ItemComposition getItemDefinition(int id);

	/**
	 * Creates an item icon sprite with passed variables.
	 *
	 * @param itemId the item ID
	 * @param quantity the item quantity
	 * @param border whether to draw a border
	 * @param shadowColor the shadow color
	 * @param stackable whether the item is stackable
	 * @param noted whether the item is noted
	 * @param scale the scale of the sprite
	 * @return the created sprite
	 */
	SpritePixels createItemSprite(int itemId, int quantity, int border, int shadowColor, int stackable, boolean noted, int scale);

	/**
	 * Loads and creates the sprite image of the passed archive and file IDs.
	 *
	 * @param source the sprite database
	 * @param archiveId the sprites archive ID
	 * @param fileId the sprites file ID
	 * @return the sprite image of the file
	 */
	SpritePixels getSprite(IndexDataBase source, int archiveId, int fileId);

	/**
	 * Gets the sprite index database.
	 *
	 * @return the sprite database
	 */
	IndexDataBase getIndexSprites();

	/**
	 * Returns the x-axis base coordinate.
	 * <p>
	 * This value is the x-axis world coordinate of tile (0, 0) in
	 * the current scene (ie. the bottom-left most coordinates in
	 * the rendered region).
	 *
	 * @return the base x-axis coordinate
	 */
	int getBaseX();

	/**
	 * Returns the y-axis base coordinate.
	 * <p>
	 * This value is the y-axis world coordinate of tile (0, 0) in
	 * the current scene (ie. the bottom-left most coordinates in
	 * the rendered region).
	 *
	 * @return the base y-axis coordinate
	 */
	int getBaseY();

	/**
	 * Gets the current mouse button that is pressed.
	 *
	 * @return the pressed mouse button
	 */
	int getMouseCurrentButton();

	/**
	 * Gets the currently selected region tile (ie. last right clicked
	 * tile).
	 *
	 * @return the selected region tile
	 */
	Tile getSelectedRegionTile();

	/**
	 * Checks whether a widget is currently being dragged.
	 *
	 * @return true if dragging a widget, false otherwise
	 */
	boolean isDraggingWidget();

	/**
	 * Gets the widget currently being dragged.
	 *
	 * @return the dragged widget, null if not dragging any widget
	 */
	Widget getDraggedWidget();

	/**
	 * Gets the widget that is being dragged on.
	 * <p>
	 * The widget being dragged has the {@link net.runelite.api.widgets.WidgetConfig#DRAG_ON}
	 * flag set, and is the widget currently under the dragged widget.
	 *
	 * @return the dragged on widget, null if not dragging any widget
	 */
	Widget getDraggedOnWidget();

	/**
	 * Sets the widget that is being dragged on.
	 *
	 * @param widget the new dragged on widget
	 */
	void setDraggedOnWidget(Widget widget);

	/**
	 * Gets the root widgets.
	 *
	 * @return the root widgets
	 */
	Widget[] getWidgetRoots();

	/**
	 * Gets a widget corresponding to the passed widget info.
	 *
	 * @param widget the widget info
	 * @return the widget
	 */
	Widget getWidget(WidgetInfo widget);

	/**
	 * Gets an array of widgets that correspond to the passed group ID.
	 *
	 * @param groupId the group ID
	 * @return the widget group
	 * @see net.runelite.api.widgets.WidgetID
	 */
	Widget[] getGroup(int groupId);

	/**
	 * Gets a widget by its raw group ID and child ID.
	 * <p>
	 * Note: Use {@link #getWidget(WidgetInfo)} for a more human-readable
	 * version of this method.
	 *
	 * @param groupId the group ID
	 * @param childId the child widget ID
	 * @return the widget corresponding to the group and child pair
	 */
	Widget getWidget(int groupId, int childId);

	/**
	 * Gets an array containing the x-axis canvas positions
	 * of all widgets.
	 *
	 * @return array of x-axis widget coordinates
	 */
	int[] getWidgetPositionsX();

	/**
	 * Gets an array containing the y-axis canvas positions
	 * of all widgets.
	 *
	 * @return array of y-axis widget coordinates
	 */
	int[] getWidgetPositionsY();

	/**
	 * Gets the current run energy of the logged in player.
	 *
	 * @return the run energy
	 */
	int getEnergy();

	/**
	 * Gets an array of options that can currently be used on other players.
	 * <p>
	 * For example, if the player is in a PVP area the "Attack" option
	 * will become available in the array. Otherwise, it won't be there.
	 *
	 * @return an array of options
	 */
	String[] getPlayerOptions();

	/**
	 * Gets an array of whether an option is enabled or not.
	 *
	 * @return the option priorities
	 */
	boolean[] getPlayerOptionsPriorities();

	/**
	 * Gets an array of player menu types.
	 *
	 * @return the player menu types
	 */
	int[] getPlayerMenuTypes();

	/**
	 * Gets a list of all RuneScape worlds.
	 *
	 * @return world list
	 */
	World[] getWorldList();

	/**
	 * Gets an array of currently open right-click menu entries that can be
	 * clicked and activated.
	 *
	 * @return array of open menu entries
	 */
	MenuEntry[] getMenuEntries();

	/**
	 * Sets the array of open menu entries.
	 * <p>
	 * This method should typically be used in the context of the {@link net.runelite.api.events.MenuOpened}
	 * event, since setting the menu entries will be overwritten the next
	 * time the menu entries are calculated.
	 *
	 * @param entries new array of open menu entries
	 */
	void setMenuEntries(MenuEntry[] entries);

	/**
	 * Checks whether a right-click menu is currently open.
	 *
	 * @return true if a menu is open, false otherwise
	 */
	boolean isMenuOpen();

	/**
	 * Gets the angle of the map, or camera yaw.
	 *
	 * @return the map angle
	 */
	int getMapAngle();

	/**
	 * Checks whether the client window is currently resized.
	 *
	 * @return true if resized, false otherwise
	 */
	boolean isResized();

	/**
	 * Gets the client revision number.
	 *
	 * @return the revision
	 */
	int getRevision();

	/**
	 * Gets an array of map region IDs that are currently loaded.
	 *
	 * @return the map regions
	 */
	int[] getMapRegions();

	/**
	 * Contains a 3D array of template chunks for instanced areas.
	 * <p>
	 * The array returned is of format [z][x][y], where z is the
	 * plane, x and y the x-axis and y-axis coordinates of a tile
	 * divided by the size of a chunk.
	 * <p>
	 * The bits of the int value held by the coordinates are structured
	 * with the following format:
	 * <ul>
	 *     <li>Bits 7 and 8 correspond to the plane</li>
	 *     <li>Bits 9 to 18 correspond to the x-axis coordinate of the chunk</li>
	 *     <li>Bits 19 to 29 correspond to the y-axis coordinate of the chunk</li>
	 *     <li>Bits 30 and 31 correspond to the chunks rotation</li>
	 * </ul>
	 * Note: The above positions assume that the left-most bit of an integer
	 * is bit position 1, and the right-most bit 32.
	 * ie.
	 * 0000 0000 0000 0000 0000 0000 0000 0000
	 *        PP XXXX XXXX XXYY YYYY YYYY YRR
	 * Where P is the plane, X and Y the x/y axis coordinates, and R the chunks
	 * rotation.
	 *
	 * @return the array of instance template chunks
	 * @see Constants#CHUNK_SIZE
	 * @see InstanceTemplates
	 */
	int[][][] getInstanceTemplateChunks();

	/**
	 * Returns a 2D array containing XTEA encryption keys used to decrypt
	 * map region files.
	 * <p>
	 * The array maps the region keys at index {@code n} to the region
	 * ID held in {@link #getMapRegions()} at {@code n}.
	 * <p>
	 * The array of keys for the region make up a 128-bit encryption key
	 * spread across 4 integers.
	 *
	 * @return the XTEA encryption keys
	 */
	int[][] getXteaKeys();

	/**
	 * Gets an array of all client variables.
	 *
	 * @return local player variables
	 */
	@VisibleForDevtools
	int[] getVarps();

	/**
	 * Gets an array of all integer client variables.
	 *
	 * @return local variables
	 */
	@VisibleForDevtools
	int[] getIntVarcs();

	/**
	 * Gets an array of all string client variables.
	 *
	 * @return local variables
	 */
	@VisibleForDevtools
	String[] getStrVarcs();

	/**
	 * Gets the value corresponding to the passed player variable.
	 *
	 * @param varPlayer the player variable
	 * @return the value
	 */
	int getVar(VarPlayer varPlayer);

	/**
	 * Gets a value corresponding to the passed variable.
	 *
	 * @param varbit the variable
	 * @return the value
	 */
	int getVar(Varbits varbit);

	/**
	 * Gets an int value corresponding to the passed variable.
	 *
	 * @param varClientInt the variable
	 * @return the value
	 */
	int getVar(VarClientInt varClientInt);

	/**
	 * Gets a String value corresponding to the passed variable.
	 *
	 * @param varClientStr the variable
	 * @return the value
	 */
	String getVar(VarClientStr varClientStr);

	/**
	 * Sets the value of a given variable.
	 *
	 * @param varbit the variable
	 * @param value the new value
	 */
	@VisibleForDevtools
	void setSetting(Varbits varbit, int value);

	/**
	 * Gets the value of a given variable.
	 *
	 * @param varps passed varbits
	 * @param varbitId the variable ID
	 * @return the value
	 * @see Varbits#id
	 */
	@VisibleForDevtools
	int getVarbitValue(int[] varps, int varbitId);

	/**
	 * Sets the value of a given variable.
	 *
	 * @param varps passed varbits
	 * @param varbit the variable
	 * @param value the value
	 * @see Varbits#id
	 */
	@VisibleForDevtools
	void setVarbitValue(int[] varps, int varbit, int value);

	/**
	 * Gets the widget flags table.
	 *
	 * @return the widget flags table
	 */
	HashTable getWidgetFlags();

	/**
	 * Gets the widget node component table.
	 *
	 * @return the widget node component table
	 * @see WidgetNode
	 */
	HashTable getComponentTable();

	/**
	 * Gets an array of current grand exchange offers.
	 *
	 * @return all grand exchange offers
	 */
	GrandExchangeOffer[] getGrandExchangeOffers();

	/**
	 * Checks whether or not a prayer is currently active.
	 *
	 * @param prayer the prayer
	 * @return true if the prayer is active, false otherwise
	 */
	boolean isPrayerActive(Prayer prayer);

	/**
	 * Gets the current experience towards a skill.
	 *
	 * @param skill the skill
	 * @return the experience
	 */
	int getSkillExperience(Skill skill);

	/**
	 * Gets the game drawing mode.
	 *
	 * @return the game drawing mode
	 */
	int getGameDrawingMode();

	/**
	 * Sets the games drawing mode.
	 *
	 * @param gameDrawingMode the new drawing mode
	 */
	void setGameDrawingMode(int gameDrawingMode);

	/**
	 * Refreshes the chat.
	 */
	void refreshChat();

	/**
	 * Gets the map of chat buffers.
	 *
	 * @return the chat buffers
	 */
	Map<Integer, ChatLineBuffer> getChatLineMap();

	/**
	 * Gets the viewport widget.
	 * <p>
	 * The viewport is the area of the game above the chatbox
	 * and to the left of the mini-map.
	 *
	 * @return the viewport widget
	 */
	Widget getViewportWidget();

	/**
	 * Gets the object composition corresponding to an objects ID.
	 *
	 * @param objectId the object ID
	 * @return the corresponding object composition
	 * @see ObjectID
	 */
	ObjectComposition getObjectDefinition(int objectId);

	/**
	 * Gets the NPC composition corresponding to an NPCs ID.
	 *
	 * @param npcId the npc ID
	 * @return the corresponding NPC composition
	 * @see NpcID
	 */
	NPCComposition getNpcDefinition(int npcId);

	/**
	 * Gets an array of all world areas
	 *
	 * @return the world areas
	 */
	Area[] getMapAreas();

	/**
	 * Gets a sprite of the map scene
	 *
	 * @return the sprite
	 */
	IndexedSprite[] getMapScene();

	/**
	 * Gets an array of currently drawn mini-map dots.
	 *
	 * @return all mini-map dots
	 */
	SpritePixels[] getMapDots();

	/**
	 * Gets the local clients game cycle.
	 * <p>
	 * Note: This value is incremented every 20ms.
	 *
	 * @return the game cycle
	 */
	int getGameCycle();

	/**
	 * Gets an array of current map icon sprites.
	 *
	 * @return the map icons
	 */
	SpritePixels[] getMapIcons();

	/**
	 * Gets an array of mod icon sprites.
	 *
	 * @return the mod icons
	 */
	IndexedSprite[] getModIcons();

	/**
	 * Replaces the current mod icons with a new array.
	 *
	 * @param modIcons the new mod icons
	 */
	void setModIcons(IndexedSprite[] modIcons);

	/**
	 * Creates an empty indexed sprite.
	 *
	 * @return the sprite
	 */
	IndexedSprite createIndexedSprite();

	/**
	 * Creates a sprite image with given width and height containing the
	 * pixels.
	 *
	 * @param pixels the pixels
	 * @param width the width
	 * @param height the height
	 * @return the sprite image
	 */
	SpritePixels createSpritePixels(int[] pixels, int width, int height);

	/**
	 * Gets the location of the local player.
	 *
	 * @return the local player location
	 */
	@Nullable
	LocalPoint getLocalDestinationLocation();

	/**
	 * Gets a list of all projectiles currently spawned.
	 *
	 * @return all projectiles
	 */
	List<Projectile> getProjectiles();

	/**
	 * Gets a list of all graphics objects currently drawn.
	 *
	 * @return all graphics objects
	 */
	List<GraphicsObject> getGraphicsObjects();

	/**
	 * Play a sound effect at the player's current location. This is how UI,
	 * and player-generated (e.g. mining, woodcutting) sound effects are
	 * normally played.
	 *
	 * @param id the ID of the sound to play. Any int is allowed, but see
	 * {@link SoundEffectID} for some common ones
	 */
	void playSoundEffect(int id);

	/**
	 * Play a sound effect from some point in the world.
	 *
	 * @param id the ID of the sound to play. Any int is allowed, but see
	 * {@link SoundEffectID} for some common ones
	 * @param x the ground coordinate on the x axis
	 * @param y the ground coordinate on the y axis
	 * @param range the number of tiles away that the sound can be heard
	 * from
	 */
	void playSoundEffect(int id, int x, int y, int range);

	/**
	 * Gets the clients graphic buffer provider.
	 *
	 * @return the buffer provider
	 */
	BufferProvider getBufferProvider();

	/**
	 * Gets the amount of ticks since the last mouse movement occurred.
	 *
	 * @return amount of idle mouse ticks
	 */
	int getMouseIdleTicks();

	/**
	 * Gets the amount of ticks since the last keyboard press occurred.
	 *
	 * @return amount of idle keyboard ticks
	 */
	int getKeyboardIdleTicks();

	/**
	 * Changes how game behaves based on memory mode. Low memory mode skips
	 * drawing of all floors and renders ground textures in low quality.
	 *
	 * @param lowMemory if we are running in low memory mode or not
	 */
	void changeMemoryMode(boolean lowMemory);

	/**
	 * Get the item container for an inventory.
	 *
	 * @param inventory the inventory type
	 * @return the item container
	 */
	@Nullable
	ItemContainer getItemContainer(InventoryID inventory);

	/**
	 * Gets the index of the last integer added to the
	 * {@link #getIntStack()} array.
	 *
	 * @return the array index
	 */
	int getIntStackSize();

	/**
	 * Sets the index of the last integer added to the
	 * {@link #getIntStack()} array.
	 *
	 * @param stackSize the array index
	 */
	void setIntStackSize(int stackSize);

	/**
	 * Gets the integer stack
	 *
	 * @return the array
	 */
	int[] getIntStack();

	/**
	 * Gets the index of the last string added to the
	 * {@link #getStringStack()} array.
	 *
	 * @return the array index
	 */
	int getStringStackSize();

	/**
	 * Sets the index of the last string added to the
	 * {@link #getStringStack()} array.
	 *
	 * @param stackSize the array index
	 */
	void setStringStackSize(int stackSize);

	/**
	 * Gets the string stack
	 *
	 * @return the string stack
	 */
	String[] getStringStack();

	/**
	 * Checks whether a player is on the friends list.
	 *
	 * @param name the name of the player
	 * @param mustBeLoggedIn if they player is online
	 * @return true if the player is friends
	 */
	boolean isFriended(String name, boolean mustBeLoggedIn);

	/**
	 * Gets the number of players in the clan chat.
	 *
	 * @return the number of clan chat members
	 */
	int getClanChatCount();

	/**
	 * Gets an array of players in the clan chat.
	 *
	 * @return the clan chat members, null if not in a clan
	 */
	ClanMember[] getClanMembers();

	/**
	 * Gets an array of players in the friends list.
	 *
	 * @return the friends list
	 */
	Friend[] getFriends();

	/**
	 * Checks whether a player is in the same clan chat.
	 *
	 * @param name the name of the player
	 * @return true if the player is in clan chat
	 */
	boolean isClanMember(String name);

	/**
	 * Gets the clients saved preferences.
	 *
	 * @return the client preferences
	 */
	Preferences getPreferences();

	/**
	 * Sets whether the camera pitch can exceed the normal limits set
	 * by the RuneScape client.
	 *
	 * @param enabled new camera pitch relaxer value
	 */
	void setCameraPitchRelaxerEnabled(boolean enabled);

	/**
	 * Gets the world map overview.
	 *
	 * @return the world map overview
	 */
	RenderOverview getRenderOverview();

	/**
	 * Checked whether the client is in stretched mode.
	 *
	 * @return true if the client is in stretched, false otherwise
	 */
	boolean isStretchedEnabled();

	/**
	 * Sets the client stretched mode state.
	 *
	 * @param state new stretched mode state
	 */
	void setStretchedEnabled(boolean state);

	/**
	 * Checks whether the client is using fast rendering techniques when
	 * stretching the client in fixed mode.
	 *
	 * @return true if client is fast rendering, false otherwise
	 */
	boolean isStretchedFast();

	/**
	 * Sets whether to use fast rendering techniques when in stretch
	 * fixed mode.
	 *
	 * @param state new fast rendering state
	 */
	void setStretchedFast(boolean state);

	/**
	 * Sets whether to force integer scale factor by rounding scale
	 * factors towards {@code zero} when stretching fixed mode.
	 *
	 * @param state new integer scaling state
	*/
	void setStretchedIntegerScaling(boolean state);

	/**
	 * Sets whether to keep aspect ratio when stretching fixed mode.
	 *
	 * @param state new keep aspect ratio state
	 */
	void setStretchedKeepAspectRatio(boolean state);

	/**
	 * Gets the current stretched dimensions of the client.
	 *
	 * @return the stretched dimensions
	 */
	Dimension getStretchedDimensions();

	/**
	 * Gets the real dimensions of the client before being stretched.
	 *
	 * @return the real dimensions
	 */
	Dimension getRealDimensions();

	/**
	 * Changes the selected world to log in to.
	 * <p>
	 * Note: this method will have no effect unless {@link GameState}
	 * is {@link GameState#LOGIN_SCREEN}.
	 *
	 * @param world the world to switch to
	 */
	void changeWorld(World world);

	/**
	 * Creates a new instance of a world.
	 *
	 * @return the created world
	 */
	World createWorld();

	/**
	 * Draws an instance map for the current viewed plane.
	 *
	 * @param z the plane
	 * @return the map sprite
	 */
	SpritePixels drawInstanceMap(int z);

	/**
	 * Runs a RuneLite script.
	 *
	 * @param id the script ID
	 * @param args additional arguments to execute the script with
	 * @see ScriptID
	 */
	void runScript(int id, Object... args);

	/**
	 * Checks whether or not there is any active hint arrow.
	 *
	 * @return true if there is a hint arrow, false otherwise
	 */
	boolean hasHintArrow();

	/**
	 * Gets the type of hint arrow currently displayed.
	 *
	 * @return the hint arrow type
	 */
	HintArrowType getHintArrowType();

	/**
	 * Clears the current hint arrow.
	 */
	void clearHintArrow();

	/**
	 * Sets a hint arrow to point to the passed location.
	 *
	 * @param point the location
	 */
	void setHintArrow(WorldPoint point);

	/**
	 * Sets a hint arrow to point to the passed player.
	 *
	 * @param player the player
	 */
	void setHintArrow(Player player);

	/**
	 * Sets a hint arrow to point to the passed NPC.
	 *
	 * @param npc the NPC
	 */
	void setHintArrow(NPC npc);

	/**
	 * Gets the world point that the hint arrow is directed at.
	 *
	 * @return hint arrow target
	 */
	WorldPoint getHintArrowPoint();

	/**
	 * Gets the player that the hint arrow is directed at.
	 *
	 * @return hint arrow target
	 */
	Player getHintArrowPlayer();

	/**
	 * Gets the NPC that the hint arrow is directed at.
	 *
	 * @return hint arrow target
	 */
	NPC getHintArrowNpc();

	/**
	 * Checks whether animation smoothing is enabled for players.
	 *
	 * @return true if player animation smoothing is enabled, false otherwise
	 */
	boolean isInterpolatePlayerAnimations();

	/**
	 * Sets the animation smoothing state for players.
	 *
	 * @param interpolate the new smoothing state
	 */
	void setInterpolatePlayerAnimations(boolean interpolate);

	/**
	 * Checks whether animation smoothing is enabled for NPC.
	 *
	 * @return true if NPC animation smoothing is enabled, false otherwise
	 */
	boolean isInterpolateNpcAnimations();

	/**
	 * Sets the animation smoothing state for NPCs.
	 *
	 * @param interpolate the new smoothing state
	 */
	void setInterpolateNpcAnimations(boolean interpolate);

	/**
	 * Checks whether animation smoothing is enabled for objects.
	 *
	 * @return true if object animation smoothing is enabled, false otherwise
	 */
	boolean isInterpolateObjectAnimations();

	/**
	 * Sets the animation smoothing state for objects.
	 *
	 * @param interpolate the new smoothing state
	 */
	void setInterpolateObjectAnimations(boolean interpolate);

	/**
	 * Checks whether the logged in player is in an instanced region.
	 *
	 * @return true if the player is in instanced region, false otherwise
	 */
	boolean isInInstancedRegion();

	/**
	 * Sets whether the client is hiding entities.
	 * <p>
	 * This method does not itself hide any entities. It behaves as a master
	 * switch for whether or not any of the related entities are hidden or
	 * shown. If this method is set to false, changing the configurations for
	 * specific entities will have no effect.
	 *
	 * @param state new entity hiding state
	 */
	void setIsHidingEntities(boolean state);

	/**
	 * Sets whether or not other players are hidden.
	 *
	 * @param state the new player hidden state
	 */
	void setPlayersHidden(boolean state);

	/**
	 * Sets whether 2D sprites (ie. overhead prayers, PK skull) related to
	 * the other players are hidden.
	 *
	 * @param state the new player 2D hidden state
	 */
	void setPlayersHidden2D(boolean state);

	/**
	 * Sets whether or not friends are hidden.
	 *
	 * @param state the new friends hidden state
	 */
	void setFriendsHidden(boolean state);

	/**
	 * Sets whether or not clan mates are hidden.
	 *
	 * @param state the new clan mates hidden state
	 */
	void setClanMatesHidden(boolean state);

	/**
	 * Sets whether the local player is hidden.
	 *
	 * @param state new local player hidden state
	 */
	void setLocalPlayerHidden(boolean state);

	/**
	 * Sets whether 2D sprites (ie. overhead prayers, PK skull) related to
	 * the local player are hidden.
	 *
	 * @param state new local player 2D hidden state
	 */
	void setLocalPlayerHidden2D(boolean state);

	/**
	 * Sets whether NPCs are hidden.
	 *
	 * @param state new NPC hidden state
	 */
	void setNPCsHidden(boolean state);

	/**
	 * Sets whether 2D sprites (ie. overhead prayers) related to
	 * the NPCs are hidden.
	 *
	 * @param state new NPC 2D hidden state
	 */
	void setNPCsHidden2D(boolean state);

	/**
	 * Sets whether attacking players or NPCs are hidden.
	 *
	 * @param state new attacker hidden state
	 */
	void setAttackersHidden(boolean state);

	/**
	 * Sets whether projectiles are hidden.
	 *
	 * @param state new projectile hidden state
	 */
	void setProjectilesHidden(boolean state);

	/**
	 * Gets an array of tile collision data.
	 * <p>
	 * The index into the array is the plane/z-axis coordinate.
	 *
	 * @return the collision data
	 */
	CollisionData[] getCollisionMaps();

	@VisibleForDevtools
	int[] getBoostedSkillLevels();

	@VisibleForDevtools
	int[] getRealSkillLevels();

	@VisibleForDevtools
	int[] getSkillExperiences();

	@VisibleForDevtools
	int[] getChangedSkills();

	@VisibleForDevtools
	int getChangedSkillsCount();

	@VisibleForDevtools
	void setChangedSkillsCount(int i);

	/**
	 * Sets a mapping of sprites to override.
	 * <p>
	 * The key value in the map corresponds to the ID of the sprite,
	 * and the value the sprite to replace it with.
	 *
	 * @param overrides the sprites to override
	 */
	void setSpriteOverrides(Map<Integer, SpritePixels> overrides);

	/**
	 * Sets a mapping of widget sprites to override.
	 * <p>
	 * The key value in the map corresponds to the packed widget ID,
	 * and the value the sprite to replace the widgets sprite with.
	 *
	 * @param overrides the sprites to override
	 */
	void setWidgetSpriteOverrides(Map<Integer, SpritePixels> overrides);

	/**
	 * Sets the compass sprite.
	 *
	 * @param spritePixels the new sprite
	 */
	void setCompass(SpritePixels spritePixels);

	/**
	 * Gets the current server tick count.
	 *
	 * @return the tick count
	 */
	int getTickCount();

	/**
	 * Sets the current server tick count.
	 *
	 * @param tickCount the new tick count
	 */
	void setTickCount(int tickCount);

	/**
	 * Sets the inventory drag delay in client game cycles (20ms).
	 *
	 * @param delay the number of game cycles to delay dragging
	 */
	void setInventoryDragDelay(int delay);

	/**
	 * Gets a set of current world types that apply to the logged in world.
	 *
	 * @return the types for current world
	 */
	EnumSet<WorldType> getWorldType();

	/**
	 * Sets the enabled state for the Oculus orb state
	 *
	 * @param state boolean enabled value
	 */
	void setOculusOrbState(int state);
}
