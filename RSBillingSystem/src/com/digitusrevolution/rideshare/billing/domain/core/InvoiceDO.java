package com.digitusrevolution.rideshare.billing.domain.core;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKLong;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.common.mapper.billing.core.InvoiceMapper;
import com.digitusrevolution.rideshare.model.billing.data.core.InvoiceEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.Invoice;

public class InvoiceDO implements DomainObjectPKLong<Invoice>{
	
	private Invoice invoice;
	private InvoiceEntity invoiceEntity;
	private final GenericDAO<InvoiceEntity, Long> genericDAO;
	private InvoiceMapper invoiceMapper;
	private static final Logger logger = LogManager.getLogger(InvoiceDO.class.getName());


	public InvoiceDO() {
		invoice = new Invoice();
		invoiceEntity = new InvoiceEntity();
		genericDAO = new GenericDAOImpl<>(InvoiceEntity.class);
		invoiceMapper = new InvoiceMapper();
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
		invoiceEntity = invoiceMapper.getEntity(invoice, true);
	}

	public void setInvoiceEntity(InvoiceEntity invoiceEntity) {
		this.invoiceEntity = invoiceEntity;
		invoice = invoiceMapper.getDomainModel(invoiceEntity, false);
	}

	@Override
	public List<Invoice> getAll() {
		List<Invoice> invoices = new ArrayList<>();
		List<InvoiceEntity> invoiceEntities = genericDAO.getAll();
		for (InvoiceEntity invoiceEntity : invoiceEntities) {
			setInvoiceEntity(invoiceEntity);
			invoices.add(invoice);
		}
		return invoices;
	}

	@Override
	public void update(Invoice invoice) {
		if (invoice.getNumber()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+invoice.getNumber());
		}
		setInvoice(invoice);
		genericDAO.update(invoiceEntity);
	}

	@Override
	public void fetchChild() {
		invoice = invoiceMapper.getDomainModelChild(invoice, invoiceEntity);
	}

	@Override
	public long create(Invoice invoice) {
		setInvoice(invoice);
		long id = genericDAO.create(invoiceEntity);
		return id;
	}

	@Override
	public Invoice get(long number) {
		invoiceEntity = genericDAO.get(number);
		if (invoiceEntity == null){
			throw new NotFoundException("No Data found with number: "+number);
		}
		setInvoiceEntity(invoiceEntity);
		return invoice;
	}

	@Override
	public Invoice getAllData(long number) {
		get(number);
		fetchChild();
		return invoice;
	}

	@Override
	public void delete(long number) {
		invoice = get(number);
		setInvoice(invoice);
		genericDAO.delete(invoiceEntity);
	}

}
