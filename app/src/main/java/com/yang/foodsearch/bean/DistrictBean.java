package com.yang.foodsearch.bean;

import java.util.List;

public class DistrictBean {
	String status;
	List<City> cities;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "DistrictBean [status=" + status + ", cities=" + cities + "]";
	}



	public List<City> getCities() {
		return cities;
	}



	public void setCities(List<City> cities) {
		this.cities = cities;
	}



	public static class City{
		String city_name;
		List<District> districts;



		@Override
		public String toString() {
			return "City [city_name=" + city_name + ", districts=" + districts + "]";
		}



		public String getCity_name() {
			return city_name;
		}



		public void setCity_name(String city_name) {
			this.city_name = city_name;
		}



		public List<District> getDistricts() {
			return districts;
		}



		public void setDistricts(List<District> districts) {
			this.districts = districts;
		}



		public static class District{

			String  district_name;
			List<String>  neighborhoods;
			public String getDistrict_name() {
				return district_name;
			}
			public void setDistrict_name(String district_name) {
				this.district_name = district_name;
			}
			public List<String> getNeighborhoods() {
				return neighborhoods;
			}
			public void setNeighborhoods(List<String> neighborhoods) {
				this.neighborhoods = neighborhoods;
			}
			@Override
			public String toString() {
				return "District [district_name=" + district_name + ", neighborhoods=" + neighborhoods + "]";
			}

		}

	}
}
