package ecommerce.app.services;

import java.util.List;

import ecommerce.app.model.Users;
import ecommerce.app.res.AddressDTO;

public interface AddressService {

    AddressDTO addAddress(AddressDTO addressDTO, Users user);

    List<AddressDTO> getAddresses(Users user);

    AddressDTO getAddressesById(Long addressId);

    AddressDTO updateAddresses(AddressDTO addressDTO, Long addressId);

    AddressDTO deleteAddress(Long addressId);

}
