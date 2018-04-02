package com.digitusrevolution.rideshare.common.mapper.billing.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.billing.data.core.InvoiceEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.Invoice;

public class InvoiceMapper implements Mapper<Invoice, InvoiceEntity>{

	@Override
	public InvoiceEntity getEntity(Invoice invoice, boolean fetchChild) {
		InvoiceEntity invoiceEntity = new InvoiceEntity();
		invoiceEntity.setNumber(invoice.getNumber());
		invoiceEntity.setDate(invoice.getDate());
		invoiceEntity.setTotalAmountEarned(invoice.getTotalAmountEarned());
		invoiceEntity.setServiceCharge(invoice.getServiceCharge());
		invoiceEntity.setCgst(invoice.getCgst());
		invoiceEntity.setSgst(invoice.getSgst());
		invoiceEntity.setIgst(invoice.getIgst());
		invoiceEntity.setTcs(invoice.getTcs());
		invoiceEntity.setStatus(invoice.getStatus());
		return invoiceEntity;
	}

	@Override
	public InvoiceEntity getEntityChild(Invoice model, InvoiceEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Invoice getDomainModel(InvoiceEntity invoiceEntity, boolean fetchChild) {
		Invoice invoice = new Invoice();
		invoice.setNumber(invoiceEntity.getNumber());
		invoice.setDate(invoiceEntity.getDate());
		invoice.setTotalAmountEarned(invoiceEntity.getTotalAmountEarned());
		invoice.setServiceCharge(invoiceEntity.getServiceCharge());
		invoice.setCgst(invoiceEntity.getCgst());
		invoice.setSgst(invoiceEntity.getSgst());
		invoice.setIgst(invoiceEntity.getIgst());
		invoice.setTcs(invoiceEntity.getTcs());
		invoice.setStatus(invoiceEntity.getStatus());
		return invoice;
	}

	@Override
	public Invoice getDomainModelChild(Invoice model, InvoiceEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Invoice> getDomainModels(Collection<Invoice> models, Collection<InvoiceEntity> entities,
			boolean fetchChild) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<InvoiceEntity> getEntities(Collection<InvoiceEntity> entities, Collection<Invoice> models,
			boolean fetchChild) {
		// TODO Auto-generated method stub
		return null;
	}

}
