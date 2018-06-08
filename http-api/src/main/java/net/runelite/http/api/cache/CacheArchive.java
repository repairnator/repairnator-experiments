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
package net.runelite.http.api.cache;

public class CacheArchive
{
	private final int archiveId;
	private final int nameHash;
	private final int revision;

	public CacheArchive(int archiveId, int nameHash, int revision)
	{
		this.archiveId = archiveId;
		this.nameHash = nameHash;
		this.revision = revision;
	}

	@Override
	public String toString()
	{
		return "CacheArchive{" + "archiveId=" + archiveId + ", nameHash=" + nameHash + ", revision=" + revision + '}';
	}

	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 71 * hash + this.archiveId;
		hash = 71 * hash + this.nameHash;
		hash = 71 * hash + this.revision;
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final CacheArchive other = (CacheArchive) obj;
		if (this.archiveId != other.archiveId)
		{
			return false;
		}
		if (this.nameHash != other.nameHash)
		{
			return false;
		}
		if (this.revision != other.revision)
		{
			return false;
		}
		return true;
	}

	public int getArchiveId()
	{
		return archiveId;
	}

	public int getNameHash()
	{
		return nameHash;
	}

	public int getRevision()
	{
		return revision;
	}
}
