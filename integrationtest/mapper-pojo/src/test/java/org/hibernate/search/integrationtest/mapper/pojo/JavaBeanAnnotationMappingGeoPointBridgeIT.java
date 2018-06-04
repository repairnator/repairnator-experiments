/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.integrationtest.mapper.pojo;

import org.hibernate.search.backend.document.model.dsl.Store;
import org.hibernate.search.engine.SearchMappingRepository;
import org.hibernate.search.engine.SearchMappingRepositoryBuilder;
import org.hibernate.search.entity.javabean.JavaBeanMapping;
import org.hibernate.search.entity.javabean.JavaBeanMappingInitiator;
import org.hibernate.search.entity.pojo.bridge.builtin.spatial.annotation.GeoPointBridge;
import org.hibernate.search.entity.pojo.bridge.builtin.spatial.annotation.Latitude;
import org.hibernate.search.entity.pojo.bridge.builtin.spatial.annotation.Longitude;
import org.hibernate.search.entity.pojo.mapping.PojoSearchManager;
import org.hibernate.search.entity.pojo.mapping.definition.annotation.AnnotationMappingDefinition;
import org.hibernate.search.entity.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.entity.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.spatial.GeoPoint;
import org.hibernate.search.spatial.ImmutableGeoPoint;
import org.hibernate.search.util.impl.integrationtest.common.rule.BackendMock;
import org.hibernate.search.util.impl.integrationtest.common.stub.backend.index.impl.StubBackendFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Yoann Rodiere
 */
public class JavaBeanAnnotationMappingGeoPointBridgeIT {

	@Rule
	public BackendMock backendMock = new BackendMock( "stubBackend" );

	private SearchMappingRepository mappingRepository;
	private JavaBeanMapping mapping;

	@Before
	public void setup() {
		SearchMappingRepositoryBuilder mappingRepositoryBuilder = SearchMappingRepository.builder()
				.setProperty( "backend.stubBackend.type", StubBackendFactory.class.getName() )
				.setProperty( "index.default.backend", "stubBackend" );

		JavaBeanMappingInitiator initiator = JavaBeanMappingInitiator.create( mappingRepositoryBuilder );

		AnnotationMappingDefinition mappingDefinition = initiator.annotationMapping();

		mappingDefinition.add( GeoPointOnTypeEntity.class );
		mappingDefinition.add( GeoPointOnCoordinatesPropertyEntity.class );
		mappingDefinition.add( GeoPointOnCustomCoordinatesPropertyEntity.class );
		mappingDefinition.add( CustomCoordinates.class );

		backendMock.expectSchema( GeoPointOnTypeEntity.INDEX, b -> b
				.field( "homeLocation", GeoPoint.class, b2 -> b2.store( Store.YES ) )
				.field( "workLocation", GeoPoint.class, b2 -> b2.store( Store.DEFAULT ) )
		);
		backendMock.expectSchema( GeoPointOnCoordinatesPropertyEntity.INDEX, b -> b
				.field( "coord", GeoPoint.class, b2 -> b2.store( Store.DEFAULT ) )
				.field( "location", GeoPoint.class, b2 -> b2.store( Store.NO ) )
		);
		backendMock.expectSchema( GeoPointOnCustomCoordinatesPropertyEntity.INDEX, b -> b
				.field( "coord", GeoPoint.class, b2 -> b2.store( Store.DEFAULT ) )
				.field( "location", GeoPoint.class, b2 -> b2.store( Store.DEFAULT ) )
		);

		mappingRepository = mappingRepositoryBuilder.build();
		mapping = initiator.getResult();
		backendMock.verifyExpectationsMet();
	}

	@After
	public void cleanup() {
		if ( mappingRepository != null ) {
			mappingRepository.close();
		}
	}

	@Test
	public void index() {
		try ( PojoSearchManager manager = mapping.createSearchManager() ) {
			GeoPointOnTypeEntity entity1 = new GeoPointOnTypeEntity();
			entity1.setId( 1 );
			entity1.setHomeLatitude( 1.1d );
			entity1.setHomeLongitude( 1.2d );
			entity1.setWorkLatitude( 1.3d );
			entity1.setWorkLongitude( 1.4d );
			GeoPointOnCoordinatesPropertyEntity entity2 = new GeoPointOnCoordinatesPropertyEntity();
			entity2.setId( 2 );
			entity2.setCoord( new GeoPoint() {
				@Override
				public double getLatitude() {
					return 2.1d;
				}

				@Override
				public double getLongitude() {
					return 2.2d;
				}
			} );
			GeoPointOnCustomCoordinatesPropertyEntity entity3 = new GeoPointOnCustomCoordinatesPropertyEntity();
			entity3.setId( 3 );
			entity3.setCoord( new CustomCoordinates( 3.1d, 3.2d ) );

			manager.getMainWorker().add( entity1 );
			manager.getMainWorker().add( entity2 );
			manager.getMainWorker().add( entity3 );

			backendMock.expectWorks( GeoPointOnTypeEntity.INDEX )
					.add( "1", b -> b
							.field( "homeLocation", new ImmutableGeoPoint(
									entity1.getHomeLatitude(), entity1.getHomeLongitude()
							) )
							.field( "workLocation", new ImmutableGeoPoint(
									entity1.getWorkLatitude(), entity1.getWorkLongitude()
							) )
					)
					.preparedThenExecuted();
			backendMock.expectWorks( GeoPointOnCoordinatesPropertyEntity.INDEX )
					.add( "2", b -> b
							.field( "coord", entity2.getCoord() )
							.field( "location", entity2.getCoord() )
					)
					.preparedThenExecuted();
			backendMock.expectWorks( GeoPointOnCustomCoordinatesPropertyEntity.INDEX )
					.add( "3", b -> b
							.field( "coord", new ImmutableGeoPoint(
									entity3.getCoord().getLat(), entity3.getCoord().getLon()
							) )
							.field( "location", new ImmutableGeoPoint(
									entity3.getCoord().getLat(), entity3.getCoord().getLon()
							) )
					)
					.preparedThenExecuted();
		}
	}

	@Indexed(index = GeoPointOnTypeEntity.INDEX)
	@GeoPointBridge(fieldName = "homeLocation", markerSet = "home", store = Store.YES)
	@GeoPointBridge(fieldName = "workLocation", markerSet = "work")
	public static final class GeoPointOnTypeEntity {

		public static final String INDEX = "GeoPointOnTypeEntity";

		private Integer id;

		private Double homeLatitude;

		private Double homeLongitude;

		private Double workLatitude;

		private Double workLongitude;

		@DocumentId
		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		@Latitude(markerSet = "home")
		public Double getHomeLatitude() {
			return homeLatitude;
		}

		public void setHomeLatitude(Double homeLatitude) {
			this.homeLatitude = homeLatitude;
		}

		@Longitude(markerSet = "home")
		public Double getHomeLongitude() {
			return homeLongitude;
		}

		public void setHomeLongitude(Double homeLongitude) {
			this.homeLongitude = homeLongitude;
		}

		@Latitude(markerSet = "work")
		public Double getWorkLatitude() {
			return workLatitude;
		}

		public void setWorkLatitude(Double workLatitude) {
			this.workLatitude = workLatitude;
		}

		@Longitude(markerSet = "work")
		public Double getWorkLongitude() {
			return workLongitude;
		}

		public void setWorkLongitude(Double workLongitude) {
			this.workLongitude = workLongitude;
		}

	}

	@Indexed(index = GeoPointOnCoordinatesPropertyEntity.INDEX)
	public static final class GeoPointOnCoordinatesPropertyEntity {

		public static final String INDEX = "GeoPointOnCoordinatesPropertyEntity";

		private Integer id;

		private GeoPoint coord;

		@DocumentId
		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		@GeoPointBridge
		@GeoPointBridge(fieldName = "location", store = Store.NO)
		public GeoPoint getCoord() {
			return coord;
		}

		public void setCoord(GeoPoint coord) {
			this.coord = coord;
		}

	}

	@Indexed(index = GeoPointOnCustomCoordinatesPropertyEntity.INDEX)
	public static final class GeoPointOnCustomCoordinatesPropertyEntity {

		public static final String INDEX = "GeoPointOnCustomCoordinatesPropertyEntity";

		private Integer id;

		private CustomCoordinates coord;

		@DocumentId
		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		@GeoPointBridge
		@GeoPointBridge(fieldName = "location")
		public CustomCoordinates getCoord() {
			return coord;
		}

		public void setCoord(CustomCoordinates coord) {
			this.coord = coord;
		}

	}

	// Does not implement GeoPoint on purpose
	public static class CustomCoordinates {

		private final Double lat;
		private final Double lon;

		public CustomCoordinates(Double lat, Double lon) {
			this.lat = lat;
			this.lon = lon;
		}

		// TODO make this work even when the property is of primitive type double
		@Latitude
		public Double getLat() {
			return lat;
		}

		@Longitude
		public Double getLon() {
			return lon;
		}
	}

}
