package com.digitusrevolution.rideshare.serviceprovider.domain.core;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.mapper.serviceprovider.core.CompanyMapper;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.CompanyEntity;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Company;
import com.digitusrevolution.rideshare.serviceprovider.data.CompanyDAO;

public class CompanyDO implements DomainObjectPKInteger<Company>{
	
	private Company company;
	private CompanyEntity companyEntity;
	private final CompanyDAO companyDAO;
	private CompanyMapper companyMapper;
	private static final Logger logger = LogManager.getLogger(CompanyDO.class.getName());
	
	public CompanyDO() {
		company = new Company();
		companyEntity = new CompanyEntity();
		companyDAO = new CompanyDAO();
		companyMapper = new CompanyMapper();
	}

	public void setCompany(Company company) {
		this.company = company;
		companyEntity = companyMapper.getEntity(company, true);
	}

	public void setCompanyEntity(CompanyEntity companyEntity) {
		this.companyEntity = companyEntity;
		company = companyMapper.getDomainModel(companyEntity, false);
	}


	@Override
	public List<Company> getAll() {
		List<Company> companies = new ArrayList<>();
		List<CompanyEntity> companyEntities = companyDAO.getAll();
		for (CompanyEntity companyEntity : companyEntities) {
			setCompanyEntity(companyEntity);
			companies.add(company);
		}
		return companies;
	}

	@Override
	public void update(Company company) {
		if (company.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+company.getId());
		}
		setCompany(company);
		companyDAO.update(companyEntity);
	}

	@Override
	public void fetchChild() {
		company = companyMapper.getDomainModelChild(company, companyEntity);
	}

	@Override
	public int create(Company company) {
		setCompany(company);
		int id = companyDAO.create(companyEntity);
		return id;
	}

	@Override
	public Company get(int id) {
		companyEntity = companyDAO.get(id);
		if (companyEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setCompanyEntity(companyEntity);
		return company;
	}

	@Override
	public Company getAllData(int id) {
		get(id);
		fetchChild();
		return company;
	}

	@Override
	public void delete(int id) {
		company = get(id);
		setCompany(company);
		companyDAO.delete(companyEntity);
	}
	
	/*
	 * Purpose - Add account to company
	 */
	public void addAccount(int companyId, Account account){
		//Reason for getting child instead of just basic entity, as if we miss any fields owned by this entity, then that would be deleted while updating
		Company company = getAllData(companyId);
		company.getAccounts().add(account);
		update(company);
	}

}

























