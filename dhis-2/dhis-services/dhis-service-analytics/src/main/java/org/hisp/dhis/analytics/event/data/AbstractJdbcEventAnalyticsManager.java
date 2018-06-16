package org.hisp.dhis.analytics.event.data;

/*
 * Copyright (c) 2004-2018, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import static org.hisp.dhis.common.DimensionalObjectUtils.COMPOSITE_DIM_OBJECT_PLAIN_SEP;
import static org.hisp.dhis.system.util.MathUtils.getRounded;
import static org.hisp.dhis.analytics.util.AnalyticsSqlUtils.quote;
import static org.hisp.dhis.analytics.util.AnalyticsSqlUtils.quoteAlias;
import static org.hisp.dhis.analytics.util.AnalyticsSqlUtils.ANALYTICS_TBL_ALIAS;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.analytics.AggregationType;
import org.hisp.dhis.analytics.EventOutputType;
import org.hisp.dhis.analytics.event.EventQueryParams;
import org.hisp.dhis.analytics.util.AnalyticsUtils;
import org.hisp.dhis.common.DimensionType;
import org.hisp.dhis.common.DimensionalObject;
import org.hisp.dhis.common.Grid;
import org.hisp.dhis.common.QueryFilter;
import org.hisp.dhis.common.QueryItem;
import org.hisp.dhis.common.ValueType;
import org.hisp.dhis.commons.util.TextUtils;
import org.hisp.dhis.jdbc.StatementBuilder;
import org.hisp.dhis.legend.Legend;
import org.hisp.dhis.option.Option;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.program.ProgramIndicator;
import org.hisp.dhis.program.ProgramIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

/**
 * @author Markus Bekken
 */
public abstract class AbstractJdbcEventAnalyticsManager
{
    private static final Log log = LogFactory.getLog( AbstractJdbcEventAnalyticsManager.class );
    
    protected static final String QUERY_ERR_MSG = "Query failed, likely because the requested analytics table does not exist";
    protected static final String ITEM_NAME_SEP = ": ";
    protected static final String NA = "[N/A]";
    protected static final String COL_COUNT = "count";
    protected static final String COL_EXTENT = "extent";
    protected static final int COORD_DEC = 6;

    protected static final int LAST_VALUE_YEARS_OFFSET = -10;
    
    @Resource( name = "readOnlyJdbcTemplate" )
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected StatementBuilder statementBuilder;
    
    @Autowired
    protected ProgramIndicatorService programIndicatorService;
    
    /**
     * Returns the dynamic select column names to use in a group by clause. Dimensions come
     * first and query items second. Program indicator expressions are converted to SQL expressions.
     * When grouping with non-default analytics period boundaries, all periods are skipped in the group
     * clause, as non default boundaries is defining their own period groups within their where clause.
     */
    protected List<String> getGroupByColumnNames( EventQueryParams params )
    {
        return getSelectColumns( params, true );
    }
    
    /**
     * Returns the dynamic select columns. Dimensions come first and query items
     * second. Program indicator expressions are converted to SQL expressions.
     * In the case of non-default boundaries{@link EventQueryParams#hasNonDefaultBoundaries},
     * the period is hard coded into the select statement with "(isoPeriod) as (periodType)".
     */
    protected List<String> getSelectColumns( EventQueryParams params )
    {
        return getSelectColumns( params, false );
    }
    
    /**
     * Returns the dynamic select columns. Dimensions come first and query items
     * second. Program indicator expressions are converted to SQL expressions.
     * In the case of non-default boundaries{@link EventQueryParams#hasNonDefaultBoundaries},
     * the period is hard coded into the select statement with "(isoPeriod) as (periodType)".
     * @param isGroupByClause used to avoid grouping by period when using non-default boundaries
     * where the column content would be hard coded. Used by the group-by calls. 
     */
    private List<String> getSelectColumns( EventQueryParams params, boolean isGroupByClause )
    {
        List<String> columns = Lists.newArrayList();
        
        for ( DimensionalObject dimension : params.getDimensions() )
        {
            if ( isGroupByClause && dimension.getDimensionType() == DimensionType.PERIOD 
                && params.hasNonDefaultBoundaries() ) 
            {
                continue;
            }
            
            if ( dimension.getDimensionType() != DimensionType.PERIOD || !params.hasNonDefaultBoundaries() )
            {
                columns.add( quote( ANALYTICS_TBL_ALIAS, dimension.getDimensionName() ) );
            }
            else if ( params.hasSinglePeriod() )
            {
                Period period = (Period) params.getPeriods().get( 0 );
                columns.add( statementBuilder.encode( period.getIsoDate() ) + " as " +
                    period.getPeriodType().getName() );
            }
            else if ( !params.hasPeriods() && params.hasFilterPeriods() )
            {
                // Assuming same period type for all period filters, as the query planner splits into one query per period type
                
                Period period = (Period) params.getFilterPeriods().get( 0 );
                columns.add( statementBuilder.encode( period.getIsoDate() ) + " as " +
                    period.getPeriodType().getName() );
            }
            else
            {
                throw new IllegalStateException( 
                    "Program indicator with non-default boundary expects queries to have exactly one period, or no periods and a period filter" );
            }
        }
        
        for ( QueryItem queryItem : params.getItems() )
        {            
            if ( queryItem.isProgramIndicator() )
            {
                ProgramIndicator in = (ProgramIndicator) queryItem.getItem();
                
                String asClause = " as " + quote( in.getUid() );
                columns.add( "(" + programIndicatorService.getAnalyticsSQl( in.getExpression(), in, params.getEarliestStartDate(), params.getLatestEndDate() ) + ")" + asClause );
            }
            else if ( ValueType.COORDINATE == queryItem.getValueType() )
            {
                String colName = quote( queryItem.getItemName() );
                
                String coordSql =  "'[' || round(ST_X(" + colName + ")::numeric, 6) || ',' || round(ST_Y(" + colName + ")::numeric, 6) || ']' as " + colName;
                
                columns.add( coordSql );
            }
            else
            {
                columns.add( quoteAlias( queryItem.getItemName() ) );
            }
        }
        
        return columns;
    }
    
    public Grid getAggregatedEventData( EventQueryParams params, Grid grid, int maxLimit )
    {
        String countClause = getAggregateClause( params );
        
        String sql = TextUtils.removeLastComma( "select " + countClause + " as value," +
            StringUtils.join( getSelectColumns( params ), "," ) + " " );

        // ---------------------------------------------------------------------
        // Criteria
        // ---------------------------------------------------------------------

        sql += getFromClause( params );
        
        sql += getWhereClause( params );

        // ---------------------------------------------------------------------
        // Group by
        // ---------------------------------------------------------------------
        
        List<String> selectColumnNames = getGroupByColumnNames( params );
        
        if ( selectColumnNames.size() > 0 )
        {
            sql += "group by " + StringUtils.join( selectColumnNames, "," ) + " ";
        }

        // ---------------------------------------------------------------------
        // Sort order
        // ---------------------------------------------------------------------

        if ( params.hasSortOrder() )
        {
            sql += "order by value " + params.getSortOrder().toString().toLowerCase() + " ";
        }
        
        // ---------------------------------------------------------------------
        // Limit, add one to max to enable later check against max limit
        // ---------------------------------------------------------------------

        if ( params.hasLimit() )
        {
            sql += "limit " + params.getLimit();
        }
        else if ( maxLimit > 0 )
        {
            sql += "limit " + ( maxLimit + 1 );
        }
        
        // ---------------------------------------------------------------------
        // Grid
        // ---------------------------------------------------------------------

        try
        {
            getAggregatedEventData( grid, params, sql );
        }
        catch ( BadSqlGrammarException ex )
        {
            log.info( QUERY_ERR_MSG, ex );
        }

        return grid;
    }
    
    private void getAggregatedEventData( Grid grid, EventQueryParams params, String sql )
    {
        log.debug( "Analytics enrollment aggregate SQL: " + sql );
        
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet( sql );

        while ( rowSet.next() )
        {            
            grid.addRow();

            if ( params.isAggregateData() )
            {
                if ( params.hasValueDimension() )
                {
                    String itemId = params.getProgram().getUid() + COMPOSITE_DIM_OBJECT_PLAIN_SEP + params.getValue().getUid();
                    grid.addValue( itemId );
                }
                else if ( params.hasProgramIndicatorDimension() )
                {
                    grid.addValue( params.getProgramIndicator().getUid() );
                }                
            }
            else
            {
                for ( QueryItem queryItem : params.getItems() )
                {
                    String itemValue = rowSet.getString( queryItem.getItemName() );
                    String gridValue = params.isCollapseDataDimensions() ? getCollapsedDataItemValue( params, queryItem, itemValue ) : itemValue;
                    grid.addValue( gridValue );
                }
            }
            
            for ( DimensionalObject dimension : params.getDimensions() )
            {
                String dimensionValue = rowSet.getString( dimension.getDimensionName() );
                grid.addValue( dimensionValue );
            }
            
            if ( params.hasValueDimension() )
            {
                double value = rowSet.getDouble( "value" );
                grid.addValue( params.isSkipRounding() ? value : getRounded( value ) );
            }
            else if ( params.hasProgramIndicatorDimension() )
            {
                double value = rowSet.getDouble( "value" );
                ProgramIndicator indicator = params.getProgramIndicator();
                grid.addValue( AnalyticsUtils.getRoundedValue( params, indicator.getDecimals(), value ) );
            }
            else
            {
                int value = rowSet.getInt( "value" );
                grid.addValue( value );
            }
            
            if ( params.isIncludeNumDen() )
            {
                grid.addNullValues( 3 );
            }
        }
    }
    
    /**
     * Returns the count clause based on value dimension and output type.
     * 
     * @param params the {@link EventQueryParams}.
     * 
     * TODO include output type if aggregation type is count
     */
    protected String getAggregateClause( EventQueryParams params )
    {
        EventOutputType outputType = params.getOutputType();
        
        if ( params.hasValueDimension() ) // TODO && isNumeric
        {
            Assert.isTrue( params.getAggregationTypeFallback().getAggregationType().isAggregateable(), "Event query aggregation type must be aggregatable" );
            
            String function = params.getAggregationTypeFallback().getAggregationType().getValue();
            
            String expression = quoteAlias( params.getValue().getUid() );
            
            return function + "(" + expression + ")";
        }
        else if ( params.hasProgramIndicatorDimension() )
        {            
            String function = params.getProgramIndicator().getAggregationTypeFallback().getValue();
            
            function = TextUtils.emptyIfEqual( function, AggregationType.CUSTOM.getValue() );
            
            String expression = programIndicatorService.getAnalyticsSQl( params.getProgramIndicator().getExpression(), 
                params.getProgramIndicator(), params.getEarliestStartDate(), params.getLatestEndDate() );
            
            return function + "(" + expression + ")";
        }
        else
        {
            if ( params.hasEnrollmentProgramIndicatorDimension() )
            {
                if ( EventOutputType.TRACKED_ENTITY_INSTANCE.equals( outputType ) && params.isProgramRegistration() )
                {
                    return "count(distinct tei)";
                }
                else // EVENT
                {
                    return "count(pi)";
                }
            }
            else
            {
                if ( EventOutputType.TRACKED_ENTITY_INSTANCE.equals( outputType ) && params.isProgramRegistration() )
                {
                    return "count(distinct " + quoteAlias( "tei") + ")";
                }
                else if ( EventOutputType.ENROLLMENT.equals( outputType ) )
                {
                    return "count(distinct " + quoteAlias( "pi") + ")";
                }
                else // EVENT
                {
                    return "count(" + quoteAlias( "psi") + ")";
                }
            }
        }
    }
    
    /**
     * Returns an item value for the given query, query item and value. Assumes that
     * data dimensions are collapsed for the given query. Returns the short name
     * of the given query item followed by the item value. If the given query item
     * has a legend set, the item value is treated as an id and substituted with
     * the matching legend name. If the given query item has an option set, the 
     * item value is treated as a code and substituted with the matching option 
     * name.
     * 
     * @param params the {@link EventQueryParams}..
     * @param item the {@link QueryItem}.
     * @param itemValue the item value.
     */
    private String getCollapsedDataItemValue( EventQueryParams params, QueryItem item, String itemValue )
    {
        String value = item.getItem().getDisplayShortName() + ITEM_NAME_SEP;
        
        Legend legend = null;
        Option option = null;
        
        if ( item.hasLegendSet() && ( legend = item.getLegendSet().getLegendByUid( itemValue ) ) != null )
        {
            return value + legend.getDisplayName();
        }        
        else if ( item.hasOptionSet() && ( option = item.getOptionSet().getOptionByCode( itemValue ) ) != null )
        {
            return value + option.getDisplayName();
        }
        else
        {
            itemValue = StringUtils.defaultString( itemValue, NA );
            
            return value + itemValue;
        }
    }

    /**
     * Returns an encoded column name wrapped in lower directive if not numeric
     * or boolean.
     * 
     * @param item the {@link QueryItem}.
     */
    protected String getColumn( QueryItem item )
    {
        String col = quoteAlias( item.getItemName() );
        return item.isText() ? "lower(" + col + ")" : col;
    }
    
    /**
     * Returns an SQL to select the expression or column of the item. If the item is 
     * a program indicator, the program indicator expression is returned - if the item 
     * is a data element, the correct column name is returned.
     * or boolean.
     * 
     * @param item the {@link QueryItem}.
     */
    protected String getSelectSql( QueryItem item, Date startDate, Date endDate )
    {
        if( item.isProgramIndicator() )
        {
            ProgramIndicator programIndicator = (ProgramIndicator)item.getItem();
            return programIndicatorService.getAnalyticsSQl( 
                programIndicator.getExpression(), programIndicator, startDate, endDate );
        }
        else
        {
            return getColumn( item );
        }
    }

    /**
     * Returns the filter value for the given query item.
     * 
     * @param filter the {@link QueryFilter}.
     * @param item the {@link QueryItem}.
     */
    protected String getSqlFilter( QueryFilter filter, QueryItem item )
    {
        String encodedFilter = statementBuilder.encode( filter.getFilter(), false );
        
        return item.getSqlFilter( filter, encodedFilter );
    }
    
    /**
     * Generate the SQL for the from-clause. Generally this means which analytics table to get data from.
     * @param params the {@link EventQueryParams} that define what is going to be queried.
     * @return SQL to add to the analytics query.
     */
    protected abstract String getFromClause( EventQueryParams params );
    
    /**
     * Generate the SQL for the where-clause. Generally this means adding filters, grouping and ordering
     * to the SQL.
     * @param params the {@link EventQueryParams} that defines the details of the filters, grouping and ordering.
     * @return SQL to add to the analytics query.
     */
    protected abstract String getWhereClause( EventQueryParams params );
}
