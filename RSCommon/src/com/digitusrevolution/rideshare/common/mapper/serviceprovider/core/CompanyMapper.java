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
		companyEntity.setServiceChargePercentage(company.getServiceChargePercentage());
		CurrencyMapper currencyMapper = new CurrencyMapper();
		companyEntity.setCurrency(currencyMapper.getEntity(company.getCurrency(), fetchChild));
		
		if (fetchChild){
			companyEntity = getEntityChild(company, companyEntity);
		}

		return companyEntity;
	}

	@Override
	public CompanyEntity getEntityChild(Company company, CompanyEntity companyEntity) {
		//Don't fetch Child of Account as it has transaction which in turn has bill and bill has copany so it will get into recursive loop
		AccountMapper accountMapper = new AccountMapper();
		companyEntity.setAccounts(accountMapper.getEntities(companyEntity.getAccounts(), company.getAccounts(), false));
		return companyEntity;
	}

	@Override
	public Company getDomainModel(CompanyEntity companyEntity, boolean fetchChild) {
		Company company = new Company();
		company.setId(companyEntity.getId());
		company.setName(companyEntity.getName());
		company.setServiceChargePercentage(companyEntity.getServiceChargePercentage());
		CurrencyMapper currencyMapper = new CurrencyMapper();
		company.setCurrency(currencyMapper.getDomainModel(companyEntity.getCurrency(), fetchChild));
		if (fetchChild){
			company = getDomainModelChild(company, companyEntity);
		}
		return company;
	}

	@Override
	public Company getDomainModelChild(Company company, CompanyEntity companyEntity) {
		//Don't fetch Child of Account as it has transaction which in turn has bill and bill has copany so it will get into recursive loop
		AccountMapper accountMapper = new AccountMapper();
		company.setAccounts(accountMapper.getDomainModels(company.getAccounts(), companyEntity.getAccounts(), false));
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
