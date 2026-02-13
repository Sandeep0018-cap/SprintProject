package com.cg.servicetest;

import com.cg.dto.VendorDto;

import com.cg.entity.Vendor;

import com.cg.exception.BadRequestException;

import com.cg.exception.ResourceNotFoundException;

import com.cg.repository.VendorRepository;
import com.cg.service.impl.VendorServiceImpl;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;

import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

/**
 * 
 * Unit tests for VendorServiceImpl (no Spring context).
 * 
 * Covers: findAll, create, delete, findById, update.
 * 
 */

@ExtendWith(MockitoExtension.class)

class VendorServiceImplTest {

	@Mock

	private VendorRepository vendorRepository;

	@InjectMocks

	private VendorServiceImpl vendorService;

	private Vendor vendor;

	@BeforeEach

	void setUp() {

		vendor = new Vendor();

		vendor.setVendorId(1L);

		vendor.setName("Tech Supplies");

		vendor.setContactName("Alice");

		vendor.setPhone("9876543210");

		vendor.setEmail("alice@techsupplies.com");

	}

	// =========================================================

	// POSITIVE TESTS (3)

	// =========================================================

	/**
	 * 
	 * findAll(): should map entities to DTOs correctly.
	 * 
	 */

	@Test

	void findAll_shouldReturnMappedDtos() {

		Vendor v2 = new Vendor();

		v2.setVendorId(2L);

		v2.setName("Office Hub");

		v2.setContactName("Bob");

		v2.setPhone("9123456789");

		v2.setEmail("bob@officehub.com");

		when(vendorRepository.findAll()).thenReturn(List.of(vendor, v2));

		List<VendorDto> dtos = vendorService.findAll();

		assertNotNull(dtos);

		assertEquals(2, dtos.size());

		assertEquals("Tech Supplies", dtos.get(0).getName());

		assertEquals("Office Hub", dtos.get(1).getName());

		verify(vendorRepository, times(1)).findAll();

	}

	/**
	 * 
	 * create(): when name is new, should save and return DTO.
	 * 
	 */

	@Test

	void create_shouldSaveAndReturnDto() {

		when(vendorRepository.findByNameIgnoreCase("Tech Supplies"))

				.thenReturn(Optional.empty());

		when(vendorRepository.save(any(Vendor.class)))

				.thenAnswer(inv -> {

					Vendor v = inv.getArgument(0);

					v.setVendorId(1L); // simulate generated id

					return v;

				});

		VendorDto dto = vendorService.create(

				"Tech Supplies", "Alice", "9876543210", "alice@techsupplies.com"

		);

		assertNotNull(dto);

		assertEquals(1L, dto.getVendorId());

		assertEquals("Tech Supplies", dto.getName());

		verify(vendorRepository).save(any(Vendor.class));

	}

	/**
	 * 
	 * update(): valid phone and existing vendor -> should update and return DTO.
	 * 
	 */

	@Test

	void update_shouldModifyAndReturnDto_whenValidPhone() {

		when(vendorRepository.findById(1L)).thenReturn(Optional.of(vendor));

		when(vendorRepository.save(any(Vendor.class))).thenAnswer(inv -> inv.getArgument(0));

		VendorDto dto = vendorService.update(

				1L, "Tech Supplies Pvt Ltd", "Alice M", "9998887776", "alice.new@ts.com"

		);

		assertNotNull(dto);

		assertEquals(1L, dto.getVendorId());

		assertEquals("Tech Supplies Pvt Ltd", dto.getName());

		assertEquals("Alice M", dto.getContactName());

		assertEquals("9998887776", dto.getPhone());

		assertEquals("alice.new@ts.com", dto.getEmail());

		verify(vendorRepository).save(any(Vendor.class));

	}

	// =========================================================

	// NEGATIVE TESTS (3)

	// =========================================================

	/**
	 * 
	 * create(): if vendor with same name exists (case-insensitive), throw
	 * BadRequestException.
	 * 
	 */

	@Test

	void create_shouldThrow_whenNameAlreadyExists() {

		when(vendorRepository.findByNameIgnoreCase("Tech Supplies"))

				.thenReturn(Optional.of(vendor));

		assertThrows(BadRequestException.class, () ->

		vendorService.create("Tech Supplies", "Alice", "9876543210", "a@b.com")

		);

		verify(vendorRepository, never()).save(any());

	}

	/**
	 * 
	 * delete(): if vendor not found, throw ResourceNotFoundException.
	 * 
	 */

	@Test

	void delete_shouldThrow_whenVendorNotFound() {

		when(vendorRepository.findById(99L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () ->

		vendorService.delete(99L)

		);

		verify(vendorRepository, never()).delete(any());

	}

	/**
	 * 
	 * update(): invalid phone (not exactly 10 digits) should throw
	 * BadRequestException.
	 * 
	 */

	@Test

	void update_shouldThrow_whenPhoneInvalid() {

		// Phone not matching \d{10}

		assertThrows(BadRequestException.class, () ->

		vendorService.update(1L, "Any", "Person", "12ab", "x@y.com")

		);

		verify(vendorRepository, never()).findById(anyLong());

		verify(vendorRepository, never()).save(any());

	}

	// =========================================================

	// (Optional) A couple of small extras—use if needed

	// =========================================================

	/**
	 * 
	 * findById(): success path.
	 * 
	 */

	@Test

	void findById_shouldReturnDto_whenExists() {

		when(vendorRepository.findById(1L)).thenReturn(Optional.of(vendor));

		VendorDto dto = vendorService.findById(1L);

		assertNotNull(dto);

		assertEquals(1L, dto.getVendorId());

		assertEquals("Tech Supplies", dto.getName());

		verify(vendorRepository).findById(1L);

	}

	/**
	 * 
	 * findById(): not found -> ResourceNotFoundException.
	 * 
	 */

	@Test

	void findById_shouldThrow_whenNotFound() {

		when(vendorRepository.findById(2L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () ->

		vendorService.findById(2L)

		);

	}

}
