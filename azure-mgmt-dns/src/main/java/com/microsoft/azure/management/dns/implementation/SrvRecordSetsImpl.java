/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */
package com.microsoft.azure.management.dns.implementation;

import com.microsoft.azure.PagedList;
import com.microsoft.azure.management.apigeneration.LangDefinition;
import com.microsoft.azure.management.dns.RecordType;
import com.microsoft.azure.management.dns.SrvRecordSet;
import com.microsoft.azure.management.dns.SrvRecordSets;
import rx.Observable;

/**
 * Implementation of SrvRecordSets.
 */
@LangDefinition
class SrvRecordSetsImpl
        extends DnsRecordSetsBaseImpl<SrvRecordSet, SrvRecordSetImpl>
        implements SrvRecordSets {

    SrvRecordSetsImpl(DnsZoneImpl dnsZone) {
        super(dnsZone, RecordType.SRV);
    }

    @Override
    public SrvRecordSetImpl getByName(String name) {
        RecordSetInner inner = this.parent().manager().inner().recordSets().get(
                this.dnsZone.resourceGroupName(),
                this.dnsZone.name(),
                name,
                this.recordType);
        if (inner == null) {
            return null;
        }
        return new SrvRecordSetImpl(inner.name(), this.dnsZone, inner);
    }

    @Override
    protected PagedList<SrvRecordSet> listIntern(String recordSetNameSuffix, Integer pageSize) {
        return super.wrapList(this.parent().manager().inner().recordSets().listByType(
                this.dnsZone.resourceGroupName(),
                this.dnsZone.name(),
                this.recordType,
                pageSize,
                recordSetNameSuffix));
    }

    @Override
    protected Observable<SrvRecordSet> listInternAsync(String recordSetNameSuffix, Integer pageSize) {
        return wrapPageAsync(this.parent().manager().inner().recordSets().listByTypeAsync(
                this.dnsZone.resourceGroupName(),
                this.dnsZone.name(),
                this.recordType));
    }

    @Override
    protected SrvRecordSetImpl wrapModel(RecordSetInner inner) {
        if (inner == null) {
            return null;
        }
        return new SrvRecordSetImpl(inner.name(), this.dnsZone, inner);
    }
}