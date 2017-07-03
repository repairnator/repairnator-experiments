/* (c) 2014 Open Source Geospatial Foundation - all rights reserved
 * (c) 2001 - 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.script.rest;

import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.geoserver.platform.resource.Resource.Type;
import org.geoserver.platform.resource.Resources;
import org.geoserver.rest.PageInfo;
import org.geoserver.rest.ReflectiveResource;
import org.geoserver.rest.format.DataFormat;
import org.geoserver.rest.format.ReflectiveHTMLFormat;
import org.geoserver.rest.format.ReflectiveXMLFormat;
import org.geoserver.script.ScriptManager;
import org.geoserver.util.Filter;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;

import com.google.common.collect.Lists;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import freemarker.template.Configuration;

/**
 * Resource that lists all available scripts as a JSON array of strings.
 * 
 * @author Justin Deoliveira, OpenGeo
 */
public class ScriptListResource extends ReflectiveResource {

    ScriptManager scriptMgr;

    String path;

    public ScriptListResource(ScriptManager scriptMgr, String path, Request request,
            Response response) {
        super(null, request, response);
        this.scriptMgr = scriptMgr;
        this.path = path;
    }

    @Override
    protected Object handleObjectGet() throws Exception {
        final String type = (String) getRequest().getAttributes().get("type");

        org.geoserver.platform.resource.Resource dir = scriptMgr.script(path);

        List<Script> scripts = Lists.newArrayList();
        if (dir != null) {
            Filter<org.geoserver.platform.resource.Resource> filter = 
                    type != null ? new Filter<org.geoserver.platform.resource.Resource>() {
                        @Override
                        public boolean accept(org.geoserver.platform.resource.Resource pathname) {
                            return type.equalsIgnoreCase(FilenameUtils.getExtension(pathname.name()));
                        }
                    } : new Filter<org.geoserver.platform.resource.Resource>() {
                        @Override
                        public boolean accept(org.geoserver.platform.resource.Resource pathname) {
                            return true;
                        }
                    };
            for (org.geoserver.platform.resource.Resource f : Resources.list(dir, filter)) {
                if (path.equals("apps")) {
                    org.geoserver.platform.resource.Resource mainScript = scriptMgr.findAppMainScript(f);
                    if (mainScript != null) {
                        String name = mainScript.path().substring(
                                f.parent().path().length() + 1).replace("\\", "/");
                        scripts.add(new Script(name));
                    }
                } else if (path.equals("wps")) {
                    if (f.getType() == Type.DIRECTORY) {
                        String namespace = f.name();
                        List<org.geoserver.platform.resource.Resource> files = f.list();
                        for(org.geoserver.platform.resource.Resource file: files) {
                            String name = namespace + ":" + file.name();
                            scripts.add(new Script(name));
                        }
                    } else {
                        String name = f.name();
                        scripts.add(new Script(name));
                    }
                } else {
                    String name = f.name();
                    scripts.add(new Script(name));
                }
            }
        } else {
            // return empty array, perhaps we should return a 404?
            // throw new RestletException(format("Could not find script dir %s",
            // path),
            // Status.CLIENT_ERROR_NOT_FOUND);
        }

        return scripts;
    }

    @Override
    protected void configureXStream(XStream xstream) {
        super.configureXStream(xstream);
        xstream.alias("script", Script.class);
        xstream.alias("scripts", List.class);

        final String name = "script";

        xstream.registerConverter(new CollectionConverter(xstream.getMapper()) {
            public boolean canConvert(Class type) {
                return Collection.class.isAssignableFrom(type);
            };

            @Override
            protected void writeItem(Object item, MarshallingContext context,
                    HierarchicalStreamWriter writer) {

                writer.startNode(name);
                context.convertAnother(item);
                writer.endNode();
            }
        });
        xstream.registerConverter(new Converter() {

            public boolean canConvert(Class type) {
                return Script.class.isAssignableFrom(type);
            }

            public void marshal(Object source, HierarchicalStreamWriter writer,
                    MarshallingContext context) {

                String name = ((Script) source).getName();
                writer.startNode("name");
                writer.setValue(name);
                writer.endNode();

                encodeLink(name, writer);
            }

            public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
                return null;
            }
        });
    }

    protected void encodeAlternateAtomLink(String link, HierarchicalStreamWriter writer) {
        encodeAlternateAtomLink(link, writer, getFormatGet());
    }

    protected void encodeAlternateAtomLink(String link, HierarchicalStreamWriter writer,
            DataFormat format) {
        writer.startNode("atom:link");
        writer.addAttribute("xmlns:atom", "http://www.w3.org/2005/Atom");
        writer.addAttribute("rel", "alternate");
        writer.addAttribute("href", href(link, format));

        if (format != null) {
            writer.addAttribute("type", format.getMediaType().toString());
        }

        writer.endNode();
    }

    protected void encodeLink(String link, HierarchicalStreamWriter writer) {
        encodeLink(link, writer, getFormatGet());
    }

    protected String href(String link, DataFormat format) {
        PageInfo pg = getPageInfo();

        // encode as relative or absolute depending on the link type
        if (link.startsWith("/")) {
            // absolute, encode from "root"
            return pg.rootURI(link);
        } else {
            // encode as relative
            return pg.pageURI(link);
        }
    }

    protected void encodeLink(String link, HierarchicalStreamWriter writer, DataFormat format) {
        if (getFormatGet() instanceof ReflectiveXMLFormat) {
            // encode as an atom link
            encodeAlternateAtomLink(link, writer, format);
        } else {
            // encode as a child element
            writer.startNode("href");
            writer.setValue(href(link, format));
            writer.endNode();
        }
    }

    @Override
    protected DataFormat createHTMLFormat(Request request, Response response) {
        return new ScriptHTMLFormat(request, response, this);
    }

    private static class ScriptHTMLFormat extends ReflectiveHTMLFormat {

        public ScriptHTMLFormat(Request request, Response response, Resource resource) {
            super(Script.class, request, response, resource);
        }

        @Override
        protected Configuration createConfiguration(Object data, Class clazz) {
            final Configuration cfg = super.createConfiguration(data, clazz);
            cfg.setClassForTemplateLoading(getClass(), "templates");
            return cfg;
        }

        @Override
        protected String getTemplateName(Object data) {
            return "script.ftl";
        }

    }

    public static class Script {

        private String name;

        public Script(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }

    }

}
