package ecommerce.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.app.model.Users;
import ecommerce.app.res.AddressDTO;
import ecommerce.app.services.AddressService;
import ecommerce.app.utils.AuthUtils;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    AuthUtils authUtils;

    @Autowired
    AddressService addressService;

    @PostMapping("/addAddress")
    public ResponseEntity<AddressDTO> addAddress(@RequestBody @Valid AddressDTO addressDTO) {
        Users user = authUtils.getLoggedInUser();
        AddressDTO address = addressService.addAddress(addressDTO,user);
        return new ResponseEntity<AddressDTO>(address, HttpStatus.CREATED);
    }
    @GetMapping("/user/addresses")
    public ResponseEntity<List<AddressDTO>> getAddresses() {
        Users user = authUtils.getLoggedInUser();
        List<AddressDTO> addresses = addressService.getAddresses(user);
        return new ResponseEntity<List<AddressDTO>>(addresses, HttpStatus.OK);
    }
    @GetMapping("/address/{addressId}")
    public ResponseEntity<AddressDTO> getAddresses(@PathVariable Long addressId) {
        AddressDTO address = addressService.getAddressesById(addressId);
        return new ResponseEntity<AddressDTO>(address, HttpStatus.OK);
    }

    @PutMapping("/address/{addressId}")
    public ResponseEntity<AddressDTO> updateAddresses(@PathVariable Long addressId,
            @RequestBody @Valid AddressDTO addressDTO) {
        AddressDTO address = addressService.updateAddresses(addressDTO, addressId);
        return new ResponseEntity<AddressDTO>(address, HttpStatus.OK);
    }
    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<AddressDTO> deleteAddresses(@PathVariable Long addressId) {
        AddressDTO address = addressService.deleteAddress(addressId);
        return new ResponseEntity<AddressDTO>(address, HttpStatus.OK);
    }
}
