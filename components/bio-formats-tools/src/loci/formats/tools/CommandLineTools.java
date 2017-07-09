/*
 * #%L
 * Bio-Formats command line tools for reading and converting files
 * %%
 * Copyright (C) 2016 - 2017 Open Microscopy Environment:
 *   - Board of Regents of the University of Wisconsin-Madison
 *   - Glencoe Software, Inc.
 *   - University of Dundee
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package loci.formats.tools;

import loci.common.DataTools;
import loci.formats.FormatTools;
import loci.formats.UpgradeChecker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An utility class for various methods used in command-line tools
 */
public final class CommandLineTools {

  // -- Constants --

  private static final Logger LOGGER = LoggerFactory.getLogger(CommandLineTools.class);
  public static final String VERSION = "-version";

  public static final String NO_UPGRADE_CHECK = "-no-upgrade";

  public static void printVersion() {
    System.out.println("Version: " + FormatTools.VERSION);
    System.out.println("Build date: " + FormatTools.DATE);
    System.out.println("VCS revision: " + FormatTools.VCS_REVISION);
  }

  public static void runUpgradeCheck(String[] args) {
    if (DataTools.indexOf(args, NO_UPGRADE_CHECK) != -1) {
      LOGGER.debug("Skipping upgrade check");
      return;
    }
    UpgradeChecker checker = new UpgradeChecker();
    boolean canUpgrade =
      checker.newVersionAvailable(UpgradeChecker.DEFAULT_CALLER);
    if (canUpgrade) {
      LOGGER.info("*** A new stable version is available. ***");
      LOGGER.info("*** Install the new version using:     ***");
      LOGGER.info("***   'upgradechecker -install'        ***");
    }
  }
}
