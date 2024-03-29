package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.user.data.StateEntity;
import com.digitusrevolution.rideshare.model.user.domain.State;

public class StateMapper implements Mapper<State, StateEntity>{

	@Override
	public StateEntity getEntity(State state, boolean fetchChild) {
		StateEntity stateEntity = new StateEntity();
		stateEntity.setId(state.getId());
		stateEntity.setName(state.getName());
		
		if (fetchChild) {
			stateEntity = getEntityChild(state, stateEntity);
		}
		return stateEntity;
	}

	@Override
	public StateEntity getEntityChild(State state, StateEntity stateEntity) {
		
		CityMapper cityMapper = new CityMapper();
		stateEntity.setCities(cityMapper.getEntities(stateEntity.getCities(), state.getCities(), true));

		return stateEntity;
	}

	@Override
	public State getDomainModel(StateEntity stateEntity, boolean fetchChild) {
		State state = new State();
		state.setId(stateEntity.getId());
		state.setName(stateEntity.getName());
		
		if (fetchChild) {
			state = getDomainModelChild(state, stateEntity);
		}
		
		return state;
	}

	@Override
	public State getDomainModelChild(State state, StateEntity stateEntity) {
		
		CityMapper cityMapper = new CityMapper();
		state.setCities(cityMapper.getDomainModels(state.getCities(), stateEntity.getCities(), true));

		return state;
	}

	@Override
	public Collection<State> getDomainModels(Collection<State> states, Collection<StateEntity> stateEntities,
			boolean fetchChild) {
		for (StateEntity stateEntity : stateEntities) {
			State state = new State();
			state = getDomainModel(stateEntity, fetchChild);
			states.add(state);
		}
		return states;
	}

	@Override
	public Collection<StateEntity> getEntities(Collection<StateEntity> stateEntities, Collection<State> states,
			boolean fetchChild) {
		for (State state : states) {
			StateEntity stateEntity = new StateEntity();
			stateEntity = getEntity(state, fetchChild);
			stateEntities.add(stateEntity);
		}
		return stateEntities;
	}

}
