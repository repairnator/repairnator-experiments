/**
 * Copyright (c) 2016-2018 Zerocracy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to read
 * the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.zerocracy.tk.project;

import com.zerocracy.Farm;
import com.zerocracy.Project;
import com.zerocracy.farm.props.Props;
import com.zerocracy.pm.cost.Estimates;
import com.zerocracy.pm.cost.Ledger;
import com.zerocracy.pmo.Catalog;
import com.zerocracy.tk.RqUser;
import com.zerocracy.tk.RsPage;
import java.io.IOException;
import org.takes.Response;
import org.takes.facets.fork.RqRegex;
import org.takes.facets.fork.TkRegex;
import org.takes.rs.xe.XeAppend;
import org.takes.rs.xe.XeChain;

/**
 * Contribution page.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.22
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class TkContribute implements TkRegex {

    /**
     * Farm.
     */
    private final Farm farm;

    /**
     * Ctor.
     * @param frm Farm
     */
    public TkContribute(final Farm frm) {
        this.farm = frm;
    }

    @Override
    @SuppressWarnings("PMD.ExcessiveMethodLength")
    public Response act(final RqRegex req) throws IOException {
        new RqUser(this.farm, req).value();
        return new RsPage(
            this.farm,
            "/xsl/contribute.xsl",
            req,
            () -> {
                final Project project = new RqProject(this.farm, req);
                final String pid = project.pid();
                return new XeChain(
                    new XeAppend("project", pid),
                    new XeAppend(
                        "title",
                        new Catalog(this.farm).bootstrap().title(pid)
                    ),
                    new XeAppend(
                        "stripe_key",
                        new Props(this.farm).get("//stripe/key", "")
                    ),
                    new XeAppend(
                        "balance",
                        new Ledger(project).bootstrap().cash().add(
                            new Estimates(project).bootstrap().total().mul(-1L)
                        ).toString()
                    )
                );
            }
        );
    }

}
