/*
 * (c) Kitodo. Key to digital objects e. V. <contact@kitodo.org>
 *
 * This file is part of the Kitodo project.
 *
 * It is licensed under GNU General Public License version 3 or later.
 *
 * For the full copyright and license information, please read the
 * LICENSE file that was distributed with this source code.
 */

package org.kitodo.mediaserver.cli;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CliApplication {

    /**
     * Starts the CLI application.
     *
     * @param args external arguments
     */
    public static void main(String[] args) {

        new SpringApplicationBuilder(CliApplication.class)
            .properties(
                "spring.config.name:"
                    + "default,"
                    + "local,"
                    + "application")
            .build().run(args);
    }
}
