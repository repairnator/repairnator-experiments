/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
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
package net.runelite.cache.script;

public class Instruction
{
	private final int opcode;
	private String name;
	private int intStackPops;
	private int stringStackPops;
	private int intStackPushes;
	private int stringStackPushes;

	public Instruction(int opcode)
	{
		this.opcode = opcode;
	}

	public int getOpcode()
	{
		return opcode;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getIntStackPops()
	{
		return intStackPops;
	}

	public void setIntStackPops(int intStackPops)
	{
		this.intStackPops = intStackPops;
	}

	public int getStringStackPops()
	{
		return stringStackPops;
	}

	public void setStringStackPops(int stringStackPops)
	{
		this.stringStackPops = stringStackPops;
	}

	public int getIntStackPushes()
	{
		return intStackPushes;
	}

	public void setIntStackPushes(int intStackPushes)
	{
		this.intStackPushes = intStackPushes;
	}

	public int getStringStackPushes()
	{
		return stringStackPushes;
	}

	public void setStringStackPushes(int stringStackPushes)
	{
		this.stringStackPushes = stringStackPushes;
	}
}
