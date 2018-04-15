<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="f" uri="http://hl7.org/fhir"/>
  <sch:ns prefix="h" uri="http://www.w3.org/1999/xhtml"/>
  <!-- 
    This file contains just the constraints for the resource DocumentReference
    It is provided for documentation purposes. When actually validating,
    always use fhir-invariants.sch (because of the way containment works)
    Alternatively you can use this file to build a smaller version of
    fhir-invariants.sch (the contents are identical; only include those 
    resources relevant to your implementation).
  -->
  <sch:pattern>
    <sch:title>Global</sch:title>
    <sch:rule context="f:extension">
      <sch:assert test="exists(f:extension)!=exists(f:*[starts-with(local-name(.), 'value')])">ext-1: Must have either extensions or value[x], not both</sch:assert>
    </sch:rule>
    <sch:rule context="f:modifierExtension">
      <sch:assert test="exists(f:extension)!=exists(f:*[starts-with(local-name(.), 'value')])">ext-1: Must have either extensions or value[x], not both</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>Global 1</sch:title>
    <sch:rule context="f:*">
      <sch:assert test="@value|f:*|h:div">global-1: All FHIR elements must have a @value or children</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>DocumentReference</sch:title>
    <sch:rule context="f:DocumentReference">
      <sch:assert test="not(parent::f:contained and f:contained)">dom-2: If the resource is contained in another resource, it SHALL NOT contain nested Resources</sch:assert>
      <sch:assert test="not(parent::f:contained and f:text)">dom-1: If the resource is contained in another resource, it SHALL NOT contain any narrative</sch:assert>
      <sch:assert test="not(exists(f:contained/*/f:meta/f:versionId)) and not(exists(f:contained/*/f:meta/f:lastUpdated))">dom-4: If a resource is contained in another resource, it SHALL NOT have a meta.versionId or a meta.lastUpdated</sch:assert>
      <sch:assert test="not(exists(for $contained in f:contained return $contained[not(parent::*/descendant::f:reference/@value=concat('#', $contained/*/id/@value) or descendant::f:reference[@value='#'])]))">dom-3: If the resource is contained in another resource, it SHALL be referred to from elsewhere in the resource or SHALL refer to the containing resource</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:text/h:div">
      <sch:assert test="not(descendant-or-self::*[not(local-name(.)=('a', 'abbr', 'acronym', 'b', 'big', 'blockquote', 'br', 'caption', 'cite', 'code', 'col', 'colgroup', 'dd', 'dfn', 'div', 'dl', 'dt', 'em', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'hr', 'i', 'img', 'li', 'ol', 'p', 'pre', 'q', 'samp', 'small', 'span', 'strong', 'sub', 'sup', 'table', 'tbody', 'td', 'tfoot', 'th', 'thead', 'tr', 'tt', 'ul', 'var'))]) and not(descendant-or-self::*/@*[not(name(.)=('abbr', 'accesskey', 'align', 'alt', 'axis', 'bgcolor', 'border', 'cellhalign', 'cellpadding', 'cellspacing', 'cellvalign', 'char', 'charoff', 'charset', 'cite', 'class', 'colspan', 'compact', 'coords', 'dir', 'frame', 'headers', 'height', 'href', 'hreflang', 'hspace', 'id', 'lang', 'longdesc', 'name', 'nowrap', 'rel', 'rev', 'rowspan', 'rules', 'scope', 'shape', 'span', 'src', 'start', 'style', 'summary', 'tabindex', 'title', 'type', 'valign', 'value', 'vspace', 'width'))])">txt-1: The narrative SHALL contain only the basic html formatting elements and attributes described in chapters 7-11 (except section 4 of chapter 9) and 15 of the HTML 4.0 standard, &lt;a&gt; elements (either name or href), images and internally contained style attributes</sch:assert>
      <sch:assert test="descendant::text()[normalize-space(.)!=''] or descendant::h:img[@src]">txt-2: The narrative SHALL have some non-whitespace content</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:masterIdentifier/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:masterIdentifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:identifier/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:subject">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:subject/f:identifier/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:subject/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:agent/f:who">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:agent/f:who/f:identifier/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:agent/f:who/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:authenticator">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:authenticator/f:identifier/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:authenticator/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:custodian">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:custodian/f:identifier/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:custodian/f:identifier//f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:relatesTo/f:target">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:relatesTo/f:target/f:identifier/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:relatesTo/f:target/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:content/f:attachment">
      <sch:assert test="not(exists(f:data)) or exists(f:contentType)">att-1: If the Attachment has data, it SHALL have a contentType</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:context/f:encounter">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:context/f:encounter/f:identifier/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:context/f:encounter/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:context/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:context/f:sourcePatientInfo">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:context/f:sourcePatientInfo/f:identifier/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:context/f:sourcePatientInfo/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:context/f:related/f:identifier/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:context/f:related/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:context/f:related/f:ref">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:context/f:related/f:ref/f:identifier/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:DocumentReference/f:context/f:related/f:ref/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::*[self::f:entry or self::f:parameter]/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a contained resource if a local reference is provided</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
