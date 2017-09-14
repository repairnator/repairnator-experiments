package de.larmic.butterfaces.component.renderkit.html_basic.text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import de.larmic.butterfaces.component.html.text.HtmlTags;
import de.larmic.butterfaces.component.partrenderer.Constants;
import de.larmic.butterfaces.component.partrenderer.ReadonlyPartRenderer;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.renderkit.html_basic.text.model.CachedNodesInitializer;
import de.larmic.butterfaces.component.renderkit.html_basic.text.part.TrivialComponentsEntriesNodePartRenderer;
import de.larmic.butterfaces.model.tree.DefaultNodeImpl;
import de.larmic.butterfaces.model.tree.Node;
import de.larmic.butterfaces.util.StringUtils;

import static de.larmic.butterfaces.component.renderkit.html_basic.text.util.FreeTextSeparators.getFreeTextSeparators;
import static de.larmic.butterfaces.component.renderkit.html_basic.text.util.TrivialComponentsUtil.createMustacheKeys;
import static de.larmic.butterfaces.component.renderkit.html_basic.text.util.TrivialComponentsUtil.replaceDotInMustacheKeys;
import static de.larmic.butterfaces.util.StringUtils.joinWithCommaSeparator;

@FacesRenderer(componentFamily = HtmlTags.COMPONENT_FAMILY, rendererType = HtmlTags.RENDERER_TYPE)
public class TagsRenderer extends AbstractHtmlTagRenderer<HtmlTags> {

    @Override
    protected boolean encodeReadonly() {
        return false;
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component, "butter-component-tags");
    }

    @Override
    protected void encodeInnerEnd(UIComponent component, ResponseWriter writer) throws IOException {
        final HtmlTags htmlTags = (HtmlTags) component;

        if (htmlTags.isReadonly()) {
            if (htmlTags.getValue() == null || StringUtils.isEmpty((String) htmlTags.getValue())) {
                new ReadonlyPartRenderer().renderReadonly(htmlTags, writer);
            } else {
                writer.startElement(ELEMENT_DIV, component);
                writer.writeAttribute("class", Constants.COMPONENT_VALUE_CLASS, null);
                super.encodeSuperEnd(FacesContext.getCurrentInstance(), component);
                writer.endElement(ELEMENT_DIV);
            }
        }
    }

    @Override
    protected void encodeEnd(HtmlTags htmlTags, ResponseWriter writer) throws IOException {
        writer.startElement("script", htmlTags);

        final String clientIdSeparator = String.valueOf(UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance()));
        final String treeBoxReadableId = htmlTags.getClientId().replace(clientIdSeparator, "_");

        writer.writeText("jQuery(function () {\n", null);

        final String jQueryBySelector = RenderUtils.createJQueryBySelector(htmlTags.getClientId(), ".butter-input-component");
        writer.writeText("var trivialTagsJQueryObject" + treeBoxReadableId + " = " + jQueryBySelector + ";\n", null);
        writer.writeText("var trivialTagsOptions" + treeBoxReadableId + " = " + createTagOptions(htmlTags) + ";\n", null);
        writer.writeText("var trivialTags" + treeBoxReadableId + " = ButterFaces.createTrivialTagComponent(" + jQueryBySelector + ",trivialTagsOptions" + treeBoxReadableId + ");\n", null);

        writer.writeText("});", null);

        writer.endElement("script");
    }

    private String createTagOptions(final HtmlTags tags) throws IOException {
        final StringBuilder options = new StringBuilder();

        final List<Node> entries = createEntries(tags.getEntries());
        final String editable = TrivialComponentsEntriesNodePartRenderer.getEditingMode(tags);
        final boolean showTrigger = !entries.isEmpty();

        options.append("{");
        options.append("\n    showTrigger: " + showTrigger + ",");
        options.append("\n    autoComplete: " + tags.isAutoComplete() + ",");
        options.append("\n    distinct: " + tags.isDistinct() + ",");
        options.append("\n    editingMode: '" + editable + "',");

        if (tags.getMaxTags() != null) {
            options.append("\n    maxSelectedEntries: " + tags.getMaxTags() + ",");
        }

        final String selectedEntries = this.getSelectedEntries(tags);
        if (StringUtils.isNotEmpty(selectedEntries)) {
            options.append("\n    selectedEntries: [" + selectedEntries + "],");
        }

        options.append("\n    freeTextSeparators: " + createFreeTextSeparators(tags) + ",");

        if (!entries.isEmpty()) {
            final List<String> mustacheKeys = createMustacheKeys(FacesContext.getCurrentInstance(), tags);
            final Map<Integer, Node> nodesMap = CachedNodesInitializer.createNodesMap(entries);
            options.append("\n    entries: " + new TrivialComponentsEntriesNodePartRenderer().renderEntriesAsJSON(entries, replaceDotInMustacheKeys(mustacheKeys), nodesMap) + ",");
        }

        options.append("\n}");

        return options.toString();
    }

    private List<Node> createEntries(final List<Object> objects) {
        final List<Node> entries = new ArrayList<>();

        if (objects != null) {
            for (Object object : objects) {
                entries.add(new DefaultNodeImpl(null, object));
            }
        }

        return entries;
    }

    private String createFreeTextSeparators(final HtmlTags tags) {
        return "[" + joinWithCommaSeparator(getFreeTextSeparators(tags), true) + "]";
    }

    private String getSelectedEntries(final HtmlTags tags) {
        final String componentValue = getSubmittedValueOrValue(tags);

        if (StringUtils.isNotEmpty(componentValue)) {
            final Iterator<String> iterator = new ArrayList<>(Arrays.asList(componentValue.split(","))).iterator();

            final StringBuilder sb = new StringBuilder();

            while (iterator.hasNext()) {
                final String next = iterator.next();
                if (StringUtils.isNotEmpty(next)) {
                    sb.append("{title:'" + escapeDisplayValue(next) + "'}");
                    if (iterator.hasNext()) {
                        sb.append(",");
                    }
                }
            }

            return sb.toString();
        }

        return null;
    }

    private String escapeDisplayValue(String value) {
        return value.replaceAll("'", "\\\\'");
    }

    private String getSubmittedValueOrValue(final HtmlTags tags) {
        if (tags.getSubmittedValue() != null) {
            return tags.getSubmittedValue().toString();
        }

        if (tags.getValue() != null) {
            return tags.getValue().toString();
        }

        return null;
    }
}
