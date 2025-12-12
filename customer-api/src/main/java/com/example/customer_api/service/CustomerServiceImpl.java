package com.example.customer_api.service;

import com.example.customer_api.dto.CustomerRequestDTO;
import com.example.customer_api.dto.CustomerResponseDTO;
import com.example.customer_api.dto.CustomerUpdateDTO;
import com.example.customer_api.entity.Customer;
import com.example.customer_api.entity.CustomerStatus;
import com.example.customer_api.exception.DuplicateResourceException;
import com.example.customer_api.exception.ResourceNotFoundException;
import com.example.customer_api.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    
    private final CustomerRepository customerRepository;
    
    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    @Override
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

//     @Override
// public Page<CustomerResponseDTO> getAllCustomers( int page, int size ) {
//     if (page < 0) page = 0;
//     if (size <= 0) size = 10; 

//     Pageable pageable = PageRequest.of(page, size);
//     Page<Customer> customerPage = customerRepository.findAll(pageable);

//     return customerPage.map(this::convertToResponseDTO);
// }


// @GetMapping
// public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers(
//         @RequestParam(required = false) String sortBy,
//         @RequestParam(defaultValue = "asc") String sortDir) {
    
//     Sort sort = sortDir.equalsIgnoreCase("asc") 
//         ? Sort.by(sortBy).ascending() 
//         : Sort.by(sortBy).descending();
    
//     List<CustomerResponseDTO> customers = customerService.getAllCustomers(sort);
//     return ResponseEntity.ok(customers);
// }    

// @Override
//     public List<CustomerResponseDTO> getAllCustomers(Sort sort) {
//         // choose a sensible default if sort is null/unspecified
//         Sort effectiveSort = (sort == null || sort.isUnsorted())
//                 ? Sort.by("createdAt").descending()   
//                 : sort;

//         List<Customer> customers = customerRepository.findAll(effectiveSort);

//         return customers.stream()
//                 .map(this::convertToResponseDTO)
//                 .collect(Collectors.toList());
//     }


// @GetMapping
// public ResponseEntity<Page<CustomerResponseDTO>> getAllCustomers(
//         @RequestParam(defaultValue = "0") int page,
//         @RequestParam(defaultValue = "10") int size,
//         @RequestParam(required = false) String sortBy,
//         @RequestParam(defaultValue = "desc") String sortDir) {

//     Page<CustomerResponseDTO> result = customerService.getAllCustomers(page, size, sortBy, sortDir);
//     return ResponseEntity.ok(result);
// }


//     @Override
// public Page<CustomerResponseDTO> getAllCustomers(int page, int size, String sortBy, String sortDir) {
//     // sanitize inputs
//     if (page < 0) page = 0;
//     if (size <= 0) size = 10;

//     // default sort field
//     String defaultSortBy = "createdAt"; // change if needed
//     String sortField = (sortBy == null || sortBy.isBlank()) ? defaultSortBy : sortBy.trim();

//     // optional whitelist to avoid invalid property names (prevents Spring errors)
//     Set<String> allowed = Set.of("id", "fullName", "email", "customerCode", "createdAt", "phone");
//     if (!allowed.contains(sortField)) {
//         sortField = defaultSortBy;
//     }

//     Sort sort = "desc".equalsIgnoreCase(sortDir)
//             ? Sort.by(sortField).descending()
//             : Sort.by(sortField).ascending();

//     Pageable pageable = PageRequest.of(page, size, sort);

//     Page<Customer> customerPage = customerRepository.findAll(pageable);

//     // convert Page<Customer> -> Page<CustomerResponseDTO>
//     return customerPage.map(this::convertToResponseDTO);
// }

    @Override
    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return convertToResponseDTO(customer);
    }
    
    @Override
    public CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO) {
        // Check for duplicates
        if (customerRepository.existsByCustomerCode(requestDTO.getCustomerCode())) {
            throw new DuplicateResourceException("Customer code already exists: " + requestDTO.getCustomerCode());
        }
        
        if (customerRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + requestDTO.getEmail());
        }
        
        // Convert DTO to Entity
        Customer customer = convertToEntity(requestDTO);
        
        // Save to database
        Customer savedCustomer = customerRepository.save(customer);
        
        // Convert Entity to Response DTO
        return convertToResponseDTO(savedCustomer);
    }
    
    @Override
    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO requestDTO) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        
        // Check if email is being changed to an existing one
        if (!existingCustomer.getEmail().equals(requestDTO.getEmail()) 
            && customerRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + requestDTO.getEmail());
        }
        
        // Update fields
        existingCustomer.setFullName(requestDTO.getFullName());
        existingCustomer.setEmail(requestDTO.getEmail());
        existingCustomer.setPhone(requestDTO.getPhone());
        existingCustomer.setAddress(requestDTO.getAddress());
        
        // Don't update customerCode (immutable)
        
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return convertToResponseDTO(updatedCustomer);
    }

    @Override
    public CustomerResponseDTO partialUpdateCustomer(Long id, CustomerUpdateDTO updateDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        if (updateDTO.getFullName() != null) {
            customer.setFullName(updateDTO.getFullName().trim());
        }

        if (updateDTO.getEmail() != null) {
            String newEmail = updateDTO.getEmail().trim();
            if (!newEmail.equalsIgnoreCase(customer.getEmail()) && customerRepository.existsByEmail(newEmail)) {
                throw new DuplicateResourceException("Email already exists: " + newEmail);
            }
            customer.setEmail(newEmail);
        }

        if (updateDTO.getPhone() != null) {
            customer.setPhone(updateDTO.getPhone().trim());
        }

        if (updateDTO.getAddress() != null) {
            customer.setAddress(updateDTO.getAddress().trim());
        }
        Customer saved = customerRepository.save(customer);
        return convertToResponseDTO(saved);
    }
    
    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }
    
    @Override
    public List<CustomerResponseDTO> searchCustomers(String keyword) {
        return customerRepository.searchCustomers(keyword)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CustomerResponseDTO> getCustomersByStatus(CustomerStatus status) {
        return customerRepository.findByStatus(status)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }


    @Override
public List<CustomerResponseDTO> advancedSearch(String name, String email, CustomerStatus status) {
    // pass-through: repository query handles null/empty logic.
    String nameParam = (name == null || name.isBlank()) ? null : name.trim();
    String emailParam = (email == null || email.isBlank()) ? null : email.trim();

    List<Customer> customers = customerRepository.advancedSearch(nameParam, emailParam, status);

    return customers.stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
}

    
    // Helper Methods for DTO Conversion
    
    private CustomerResponseDTO convertToResponseDTO(Customer customer) {
        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setId(customer.getId());
        dto.setCustomerCode(customer.getCustomerCode());
        dto.setFullName(customer.getFullName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setAddress(customer.getAddress());
        dto.setStatus(customer.getStatus().toString());
        dto.setCreatedAt(customer.getCreatedAt());
        return dto;
    }
    
    private Customer convertToEntity(CustomerRequestDTO dto) {
        Customer customer = new Customer();
        customer.setCustomerCode(dto.getCustomerCode());
        customer.setFullName(dto.getFullName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        return customer;
    }

    
}
