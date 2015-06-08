package com.mgw.member.ui.activity.cityleague.model;

import java.util.ArrayList;
import java.util.List;

public class CityList {
	public List<City> HOT;
	public List<City> A;
	public List<City> B;
	public List<City> C;
	public List<City> D;
	public List<City> E;
	public List<City> F;
	public List<City> G;
	public List<City> H;
	public List<City> J;
	public List<City> K;
	public List<City> L;
	public List<City> M;
	public List<City> N;
	public List<City> Q;
	public List<City> R;
	public List<City> S;
	public List<City> T;
	public List<City> W;
	public List<City> X;
	public List<City> Y;
	public List<City> Z;
	
	public static class City 
	{
		public String key;
		public String name;
	}
	
	public  List<CityModel> toModelList(){
		
		List<CityModel> names = new ArrayList<CityModel>();
		
		for(int i=0;i<HOT.size();i++){
			CityModel cityModel = new CityModel();
			City city=(City)HOT.get(i);
			cityModel.setCityCode(city.key);
			cityModel.setNameSort("HOT");
			cityModel.setCityName(city.name);
			names.add(cityModel);
		}
		
		for(int i=0;i<A.size();i++){
			CityModel cityModel = new CityModel();
			City city=(City)A.get(i);
			cityModel.setNameSort("A");
			cityModel.setCityCode(city.key);
			cityModel.setCityName(city.name);
			names.add(cityModel);
		}
		
		for(int i=0;i<B.size();i++){
			CityModel cityModel = new CityModel();
			City city=(City)B.get(i);
			cityModel.setCityCode(city.key);
			cityModel.setNameSort("B");
			cityModel.setCityName(city.name);
			names.add(cityModel);
		}
		
		for(int i=0;i<C.size();i++){
			CityModel cityModel = new CityModel();
			City city=(City)C.get(i);
			cityModel.setCityCode(city.key);
			cityModel.setNameSort("C");
			cityModel.setCityName(city.name);
			names.add(cityModel);
		}
		
		for(int i=0;i<D.size();i++){
			CityModel cityModel = new CityModel();
			City city=(City)D.get(i);
			cityModel.setCityCode(city.key);
			cityModel.setNameSort("D");
			cityModel.setCityName(city.name);
			names.add(cityModel);
		}
		
		for(int i=0;i<E.size();i++){
			CityModel cityModel = new CityModel();
			City city=(City)E.get(i);
			cityModel.setCityCode(city.key);
			cityModel.setNameSort("E");
			cityModel.setCityName(city.name);
			names.add(cityModel);
		}
		
		for(int i=0;i<F.size();i++){
			CityModel cityModel = new CityModel();
			City city=(City)F.get(i);
			cityModel.setNameSort("F");
			cityModel.setCityCode(city.key);
			cityModel.setCityName(city.name);
			names.add(cityModel);
		}
		
		for(int i=0;i<G.size();i++){
			CityModel cityModel = new CityModel();
			City city=(City)G.get(i);
			cityModel.setNameSort("G");
			cityModel.setCityCode(city.key);
			cityModel.setCityName(city.name);
			names.add(cityModel);
		}
		for(int i=0;i<H.size();i++){
			CityModel cityModel = new CityModel();
			City city=(City)H.get(i);
			cityModel.setNameSort("H");
			cityModel.setCityCode(city.key);
			cityModel.setCityName(city.name);
			names.add(cityModel);
		}
		for(int i=0;i<J.size();i++){
			CityModel cityModel = new CityModel();
			City city=(City)J.get(i);
			cityModel.setNameSort("J");
			cityModel.setCityCode(city.key);
			cityModel.setCityName(city.name);
			names.add(cityModel);
		}
		for(int i=0;i<K.size();i++){
			CityModel cityModel = new CityModel();
			City city=(City)K.get(i);
			cityModel.setNameSort("K");
			cityModel.setCityCode(city.key);
			cityModel.setCityName(city.name);
			names.add(cityModel);
		}
		
		for(int i=0;i<L.size();i++){
			CityModel cityModel = new CityModel();
			City city=(City)L.get(i);
			cityModel.setNameSort("L");
			cityModel.setCityCode(city.key);
			cityModel.setCityName(city.name);
			names.add(cityModel);
		}
		
		for(int i=0;i<M.size();i++){
			CityModel cityModel = new CityModel();
			City city=(City)M.get(i);
			cityModel.setNameSort("M");
			cityModel.setCityCode(city.key);
			cityModel.setCityName(city.name);
			names.add(cityModel);
		}
		
		
		for(int i=0;i<N.size();i++){
			CityModel cityModel = new CityModel();
			City city=(City)N.get(i);
			cityModel.setNameSort("N");
			cityModel.setCityCode(city.key);
			cityModel.setCityName(city.name);
			names.add(cityModel);
		}
		
		
		for(int i=0;i<Q.size();i++){
			CityModel cityModel = new CityModel();
			City city=(City)Q.get(i);
			cityModel.setNameSort("Q");
			cityModel.setCityCode(city.key);
			cityModel.setCityName(city.name);
			names.add(cityModel);
		}
		
		
		for(int i=0;i<R.size();i++){
			CityModel cityModel = new CityModel();
			City city=(City)R.get(i);
			cityModel.setNameSort("R");
			cityModel.setCityCode(city.key);
			cityModel.setCityName(city.name);
			names.add(cityModel);
		}
		for(int i=0;i<S.size();i++){
			CityModel cityModel = new CityModel();
			City city=(City)S.get(i);
			cityModel.setNameSort("S");
			cityModel.setCityCode(city.key);
			cityModel.setCityName(city.name);
			names.add(cityModel);
		}
		for(int i=0;i<T.size();i++){
			CityModel cityModel = new CityModel();
			City city=(City)T.get(i);
			cityModel.setNameSort("T");
			cityModel.setCityCode(city.key);
			cityModel.setCityName(city.name);
			names.add(cityModel);
		}
		for(int i=0;i<W.size();i++){
			CityModel cityModel = new CityModel();
			City city=(City)W.get(i);
			cityModel.setNameSort("W");
			cityModel.setCityCode(city.key);
			cityModel.setCityName(city.name);
			names.add(cityModel);
		}
		for(int i=0;i<X.size();i++){
			CityModel cityModel = new CityModel();
			City city=(City)X.get(i);
			cityModel.setNameSort("X");
			cityModel.setCityCode(city.key);
			cityModel.setCityName(city.name);
			names.add(cityModel);
		}
		for(int i=0;i<Y.size();i++){
			CityModel cityModel = new CityModel();
			City city=(City)Y.get(i);
			cityModel.setNameSort("Y");
			cityModel.setCityCode(city.key);
			cityModel.setCityName(city.name);
			names.add(cityModel);
		}
		for(int i=0;i<Z.size();i++){
			CityModel cityModel = new CityModel();
			City city=(City)Z.get(i);
			cityModel.setNameSort("Z");
			cityModel.setCityCode(city.key);
			cityModel.setCityName(city.name);
			names.add(cityModel);
		}
		
		return names;
		
		
		
	}

}
