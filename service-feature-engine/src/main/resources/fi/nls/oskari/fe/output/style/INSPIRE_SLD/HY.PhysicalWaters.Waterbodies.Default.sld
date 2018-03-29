<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor version="1.1.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:sld="http://www.opengis.net/sld" xmlns:se="http://www.opengis.net/se">      
 <sld:NamedLayer>
    <se:Name>HY.PhysicalWaters.Waterbodies</se:Name>
    <sld:UserStyle>
      <se:Name> HY.PhysicalWaters.Waterbodies.Default</se:Name>
      <sld:IsDefault>1</sld:IsDefault>
      <se:FeatureTypeStyle version="1.1.0">
        <se:Description>
          <se:Title>Water bodies default style</se:Title>
          <se:Abstract>Physical waters as watercourses or standing water can be portrayed with different geometries depending on its dimensions and the level of detail or resolution. Lineal watercourses are depicted by solid blue (#33CCFF) lines with stroke width of 1 pixel and the superficial ones are depicted by filled blue light polygons (#CCFFFF) without border. Punctual standing waters are depicted by dark blue (#0066FF) circles with size of 6 pixel and the superficial ones are depicted by filled blue light polygons (#CCFFFF) without border.</se:Abstract>
        </se:Description>
        <se:FeatureTypeName>PhysicalWaters.Watercourse</se:FeatureTypeName>
        <se:Rule>
          <ogc:Filter>
            <!--Delineation is known-->
            <se:PropertyIsEqualTo>
               <ogc:PropertyName>delineationKnown</ogc:PropertyName>
              <ogc:Literal>true</ogc:Literal>
            </se:PropertyIsEqualTo>
          </ogc:Filter>
          <se:LineSymbolizer>
            <se:Geometry>
              <ogc:PropertyName>geometry</ogc:PropertyName>
            </se:Geometry>
            <se:Stroke>
					<se:SvgParameter name="stroke">#33CCFF</se:SvgParameter>
					<se:SvgParameter name="stroke-width">1</se:SvgParameter>
            </se:Stroke>
          </se:LineSymbolizer>
          <se:PolygonSymbolizer>
            <se:Geometry>
              <ogc:PropertyName>geometry</ogc:PropertyName>
            </se:Geometry>
            <se:Fill>
					<se:SvgParameter name="fill">#CCFFFF</se:SvgParameter>
            </se:Fill>
          </se:PolygonSymbolizer>
        </se:Rule>
        <se:FeatureTypeName>PhysicalWaters.StandingWater</se:FeatureTypeName>
        <se:Rule>
          <se:PointSymbolizer>
            <se:Geometry>
              <ogc:PropertyName>geometry</ogc:PropertyName>
            </se:Geometry>
            <se:Graphic>
              <se:Mark>
                <se:WellKnownName>circle</se:WellKnownName>
                <se:Fill>
				     <se:SvgParameter name="fill">#0066FF</se:SvgParameter>
                </se:Fill>
              </se:Mark>
              <se:Size>
			       <se:SvgParameter name="size">6</se:SvgParameter>
              </se:Size>
            </se:Graphic>
          </se:PointSymbolizer>
          <se:PolygonSymbolizer>
            <se:Geometry>
              <ogc:PropertyName>geometry</ogc:PropertyName>
            </se:Geometry>
            <se:Fill>
					<se:SvgParameter name="fill">#CCFFFF</se:SvgParameter>
            </se:Fill>
          </se:PolygonSymbolizer>
        </se:Rule>
      </se:FeatureTypeStyle>
    </sld:UserStyle>
  </sld:NamedLayer>
</StyledLayerDescriptor>
