package com.digitusrevolution.rideshare.serviceprovider.domain.core;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.CompanyEntity;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Company;
import com.digitusrevolution.rideshare.serviceprovider.data.CompanyDAO;

public class CompanyDO implements DomainObjectPKInteger<Company>{
	
	private Company company;
	private final CompanyDAO companyDAO;
	private static final Logger logger = LogManager.getLogger(CompanyDO.class.getName());
	
	public CompanyDO() {
		company = new Company();
		companyDAO = new CompanyDAO();
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public List<Company> getAll() {
		List<Company> companies = new ArrayList<>();
		List<CompanyEntity> companyEntities = companyDAO.getAll();
		for (CompanyEntity companyEntity : companyEntities) {
			Company company = new Company();
			company.setEntity(companyEntity);
			companies.add(company);
		}
		return companies;
	}

	@Override
	public void update(Company company) {
		if (company.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+company.getId());
		}
		companyDAO.update(company.getEntity());
	}

	@Override
	public int create(Company company) {
		int id = companyDAO.create(company.getEntity());
		return id;
	}

	@Override
	public Company get(int id) {
		CompanyEntity companyEntity = companyDAO.get(id);
		if (companyEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		company.setEntity(companyEntity);
		return company;
	}

	@Override
	public void delete(int id) {
		company = get(id);
		companyDAO.delete(company.getEntity());
	}
	
	/*
	 * Purpose - Add account to company
	 */
	public void addAccount(int companyId, Account account){
		Company company = get(companyId);
		company.getAccounts().add(account);
		company.setAccounts(company.getAccounts());
		update(company);
	}

}

























