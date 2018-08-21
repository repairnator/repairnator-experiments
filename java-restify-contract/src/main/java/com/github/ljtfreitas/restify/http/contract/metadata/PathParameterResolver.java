/*******************************************************************************
 *
 * MIT License
 *
 * Copyright (c) 2016 Tiago de Freitas Lima
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *******************************************************************************/
package com.github.ljtfreitas.restify.http.contract.metadata;

import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;

class PathParameterResolver {

	private final String path;
	private final EndpointMethodParameters parameters;

	public PathParameterResolver(String path, EndpointMethodParameters parameters) {
		this.path = path;
		this.parameters = parameters;
	}

	public String resolve(Object[] args) {
		StringBuilder endpoint = new StringBuilder();

		endpoint.append(path(args));

		return endpoint.toString();
	}

	private String path(Object[] args) {
		StringBuffer builder = new StringBuffer();

		Matcher matcher = DynamicParameterMatcher.matches(path);

		while (matcher.find()) {
			MatchResult match = matcher.toMatchResult();

			String name = match.group(1);

			parameters.find(name)
				.filter(p -> p.path())
					.ifPresent(p -> matcher.appendReplacement(builder,
							Optional.ofNullable(args[p.position()]).map(a -> p.resolve(a))
								.orElseThrow(() -> new IllegalArgumentException("Your path argument [" + name + "] cannot be null."))));
		}

		matcher.appendTail(builder);

		return builder.toString();
	}
}
