package ecommerce.app.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecommerce.app.data.AddressRepository;
import ecommerce.app.data.UserRepository;
import ecommerce.app.err.ResourceNotFoundException;
import ecommerce.app.model.Addresses;
import ecommerce.app.model.Users;
import ecommerce.app.res.AddressDTO;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;
    @Override
    public AddressDTO addAddress(AddressDTO addressDTO, Users user) {
        Addresses users_address = modelMapper.map(addressDTO, Addresses.class); 
        List<Addresses> addresses = user.getAddresses();
        addresses.add(users_address);
        user.setAddresses(addresses);
        users_address.setUsers(user);
        Addresses savedAddress = addressRepository.save(users_address);
        userRepository.save(user);
        AddressDTO addressDTOSaved = modelMapper.map(savedAddress, AddressDTO.class);

        return addressDTOSaved;
    }
    @Override
    public List<AddressDTO> getAddresses(Users user) {
        List<Addresses> addresses = user.getAddresses();
        List<AddressDTO> addressDTOs = addresses.stream().map(address -> modelMapper.map(address,AddressDTO.class)).toList();
        return addressDTOs;
    }
    @Override
    public AddressDTO getAddressesById(Long addressId) {
        Addresses address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        AddressDTO addressDTO = modelMapper.map(address, AddressDTO.class);
        return addressDTO;
    }
    @Override
    public AddressDTO updateAddresses(AddressDTO addressDTO, Long addressId) {
        Addresses address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());        
        address.setState(addressDTO.getState());
        address.setCountry(addressDTO.getCountry());
        address.setZipCode(addressDTO.getZipCode());
        Addresses updatedAddress = addressRepository.save(address);
        AddressDTO addressDTOs = modelMapper.map(updatedAddress, AddressDTO.class);
        return addressDTOs;
    }
    @Override
    public AddressDTO deleteAddress(Long addressId) {
        Addresses address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        addressRepository.deleteById(addressId);
        return modelMapper.map(address, AddressDTO.class);
    }

}
