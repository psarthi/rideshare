package com.digitusrevolution.rideshare.common.mapper.serviceprovider.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.billing.core.AccountMapper;
import com.digitusrevolution.rideshare.common.mapper.user.CurrencyMapper;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.CompanyEntity;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Company;

public class CompanyMapper implements Mapper<Company, CompanyEntity>{

	@Override
	public CompanyEntity getEntity(Company company, boolean fetchChild) {
		CompanyEntity companyEntity = new CompanyEntity();
		companyEntity.setId(company.getId());
		companyEntity.setName(company.getName());
		AccountMapper accountMapper = new AccountMapper();
		if (company.getAccount()!=null) companyEntity.setAccount(accountMapper.getEntity(company.getAccount(), fetchChild));
		CurrencyMapper currencyMapper = new CurrencyMapper();
		companyEntity.setCurrency(currencyMapper.getEntity(company.getCurrency(), fetchChild));
		return companyEntity;
	}

	@Override
	public CompanyEntity getEntityChild(Company company, CompanyEntity companyEntity) {
		return companyEntity;
	}

	@Override
	public Company getDomainModel(CompanyEntity companyEntity, boolean fetchChild) {
		Company company = new Company();
		company.setId(companyEntity.getId());
		company.setName(companyEntity.getName());
		AccountMapper accountMapper = new AccountMapper();
		if (companyEntity.getAccount()!=null)  company.setAccount(accountMapper.getDomainModel(companyEntity.getAccount(), fetchChild));
		CurrencyMapper currencyMapper = new CurrencyMapper();
		company.setCurrency(currencyMapper.getDomainModel(companyEntity.getCurrency(), fetchChild));
		return company;
	}

	@Override
	public Company getDomainModelChild(Company company, CompanyEntity companyEntity) {
		return company;
	}

	@Override
	public Collection<Company> getDomainModels(Collection<Company> companies, Collection<CompanyEntity> companyEntities,
			boolean fetchChild) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<CompanyEntity> getEntities(Collection<CompanyEntity> companyEntities, Collection<Company> companies,
			boolean fetchChild) {
		// TODO Auto-generated method stub
		return null;
	}

}
