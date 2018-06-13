package com.digitusrevolution.rideshare.common.mapper.serviceprovider.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.billing.core.AccountMapper;
import com.digitusrevolution.rideshare.common.mapper.user.CityMapper;
import com.digitusrevolution.rideshare.common.mapper.user.CountryMapper;
import com.digitusrevolution.rideshare.common.mapper.user.CurrencyMapper;
import com.digitusrevolution.rideshare.common.mapper.user.StateMapper;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.CompanyEntity;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Company;
import com.digitusrevolution.rideshare.model.user.domain.Country;

public class CompanyMapper implements Mapper<Company, CompanyEntity>{

	@Override
	public CompanyEntity getEntity(Company company, boolean fetchChild) {
		CompanyEntity companyEntity = new CompanyEntity();
		companyEntity.setId(company.getId());
		companyEntity.setName(company.getName());
		companyEntity.setAddress(company.getAddress());
		CountryMapper countryMapper = new CountryMapper();
		companyEntity.setCountry(countryMapper.getEntity(company.getCountry(), fetchChild));
		StateMapper stateMapper = new StateMapper();
		companyEntity.setState(stateMapper.getEntity(company.getState(), fetchChild));
		companyEntity.setServiceChargePercentage(company.getServiceChargePercentage());
		CurrencyMapper currencyMapper = new CurrencyMapper();
		companyEntity.setCurrency(currencyMapper.getEntity(company.getCurrency(), fetchChild));
		companyEntity.setServiceChargePercentage(company.getServiceChargePercentage());
		companyEntity.setCgstPercentage(company.getCgstPercentage());
		companyEntity.setSgstPercentage(company.getSgstPercentage());
		companyEntity.setIgstPercentage(company.getIgstPercentage());
		companyEntity.setTcsPercentage(company.getTcsPercentage());
		companyEntity.setGstNumber(company.getGstNumber());
		companyEntity.setGstCode(company.getGstCode());
		companyEntity.setPan(company.getPan());
		
		CityMapper cityMapper = new CityMapper();
		companyEntity.setOperatingCities(cityMapper.getEntities(companyEntity.getOperatingCities(), company.getOperatingCities(), true));
		
		if (fetchChild){
			companyEntity = getEntityChild(company, companyEntity);
		}

		return companyEntity;
	}

	@Override
	public CompanyEntity getEntityChild(Company company, CompanyEntity companyEntity) {
		//Don't fetch Child of Account as it has transaction which in turn has bill and bill has company so it will get into recursive loop
		AccountMapper accountMapper = new AccountMapper();
		companyEntity.setAccounts(accountMapper.getEntities(companyEntity.getAccounts(), company.getAccounts(), false));
		
		OfferMapper offerMapper = new OfferMapper();
		companyEntity.setOffers(offerMapper.getEntities(companyEntity.getOffers(), company.getOffers(), true));
		
		PartnerMapper partnerMapper = new PartnerMapper();
		companyEntity.setPartners(partnerMapper.getEntities(companyEntity.getPartners(), company.getPartners(), true));
		
		return companyEntity;
	}

	@Override
	public Company getDomainModel(CompanyEntity companyEntity, boolean fetchChild) {
		Company company = new Company();
		company.setId(companyEntity.getId());
		company.setName(companyEntity.getName());
		company.setAddress(companyEntity.getAddress());
		CountryMapper countryMapper = new CountryMapper();
		company.setCountry(countryMapper.getDomainModel(companyEntity.getCountry(), fetchChild));
		StateMapper stateMapper = new StateMapper();
		company.setState(stateMapper.getDomainModel(companyEntity.getState(), fetchChild));
		company.setServiceChargePercentage(companyEntity.getServiceChargePercentage());
		CurrencyMapper currencyMapper = new CurrencyMapper();
		company.setCurrency(currencyMapper.getDomainModel(companyEntity.getCurrency(), fetchChild));
		company.setServiceChargePercentage(companyEntity.getServiceChargePercentage());
		company.setCgstPercentage(companyEntity.getCgstPercentage());
		company.setSgstPercentage(companyEntity.getSgstPercentage());
		company.setIgstPercentage(companyEntity.getIgstPercentage());
		company.setTcsPercentage(companyEntity.getTcsPercentage());
		company.setGstNumber(companyEntity.getGstNumber());
		company.setGstCode(companyEntity.getGstCode());
		company.setPan(companyEntity.getPan());
		
		CityMapper cityMapper = new CityMapper();
		company.setOperatingCities(cityMapper.getDomainModels(company.getOperatingCities(), companyEntity.getOperatingCities(), true));
		
		if (fetchChild){
			company = getDomainModelChild(company, companyEntity);
		}
		return company;
	}

	@Override
	public Company getDomainModelChild(Company company, CompanyEntity companyEntity) {
		//Don't fetch Child of Account as it has transaction which in turn has bill and bill has company so it will get into recursive loop
		AccountMapper accountMapper = new AccountMapper();
		company.setAccounts(accountMapper.getDomainModels(company.getAccounts(), companyEntity.getAccounts(), false));
		
		OfferMapper offerMapper = new OfferMapper();
		company.setOffers(offerMapper.getDomainModels(company.getOffers(), companyEntity.getOffers(), true));
		
		PartnerMapper partnerMapper = new PartnerMapper();
		company.setPartners(partnerMapper.getDomainModels(company.getPartners(), companyEntity.getPartners(), true));
		
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
