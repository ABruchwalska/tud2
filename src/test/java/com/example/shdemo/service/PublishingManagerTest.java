package com.example.shdemo.service;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.example.shdemo.domain.Ksiazka;
import com.example.shdemo.domain.Autor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/beans.xml" })
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class PublishingManagerTest {

	@Autowired
	PublishingManager pM;

	private final String IMIE_1 	= "Ash";
	private final int WIEK_1			= 10;
	private final String COUNTRY_1 	= "Johto";

	private final String IMIE_2 	= "Gary";
	private final String COUNTRY_2 	= "Kanto";

	private final String TYTUL_1 	= "Pikachu"; //?
	private final String AUTOR_1 		= "Ash"; //?!?!?!??!?

	private final String TYTUL_2 	= "Squirtle";
	private final String AUTOR_2 		= "Gary";

	@Test
	public void addAutorCheck() {

		Autor autor = new Autor();
		autor.setImie(IMIE_1);
		autor.setWiek(WIEK_1);
		autor.setCountry(COUNTRY_1);

		Long AutorId = pM.addAutor(autor);

		Autor retrievedAutor = pM.findAutorById(AutorId);

		assertEquals(IMIE_1, retrievedAutor.getImie());
		assertEquals(WIEK_1, retrievedAutor.getWiek());
		assertEquals(COUNTRY_1, retrievedAutor.getCountry());

	}

	@Test
	public void addKsiazkaCheck() {

		Ksiazka ksiazka= new Ksiazka();
		Autor autor = new Autor();
		ksiazka.setTytul(TYTUL_1);
		ksiazka.setAutor(AUTOR_1);
		
		autor.setImie(IMIE_1);
		autor.setWiek(WIEK_1);
		autor.setCountry(COUNTRY_1);
		
		Long KsiazkaId = pM.addNewKsiazka(ksiazka, autor);

		Ksiazka retrievedKsiazka = pM.findKsiazkaById(KsiazkaId);
		assertEquals(TYTUL_1, retrievedKsiazka.getTytul());
		assertEquals(AUTOR_1, retrievedKsiazka.getAutor());

	}

	@Test
	public void getAvailableKsiazkasCheck() {

		Autor autor = new Autor();
		autor.setImie(IMIE_2);
		autor.setCountry(COUNTRY_2);

		Long AutorId = pM.addAutor(autor);

		Autor retrievedAutor = pM.findAutorById(AutorId);

		Ksiazka ksiazka = new Ksiazka();
		ksiazka.setTytul(TYTUL_2);
		ksiazka.setAutor(AUTOR_2);

		pM.addNewKsiazka(ksiazka, autor);

	
		List<Ksiazka> getAvailableKsiazkas = pM.getAvailableKsiazkas();

		assertEquals(1, getAvailableKsiazkas.size());
		assertEquals(TYTUL_2, getAvailableKsiazkas.get(0).getTytul());
		assertEquals(AUTOR_2, getAvailableKsiazkas.get(0).getAutor());
	}

	@Test
	public void unpublishKsiazkaCheck() {
		
		Autor autor = new Autor();
		autor.setImie(IMIE_1);
		autor.setWiek(WIEK_1);
		autor.setCountry(COUNTRY_1);
		
		Long AutorId = pM.addAutor(autor);
		
		Ksiazka ksiazka = new Ksiazka();
		ksiazka.setTytul(TYTUL_1);
		ksiazka.setAutor(AUTOR_1);
		
		pM.addNewKsiazka(ksiazka, autor);
		
		pM.unpublishKsiazka(autor, ksiazka);
		
		Autor retrievedAutor = pM.findAutorById(AutorId);
		assertEquals(1, retrievedAutor.getKsiazkas().size());
		
		pM.unpublishKsiazka(retrievedAutor, ksiazka);
		assertEquals(0, retrievedAutor.getKsiazkas().size());
	}
	
	@Test
	public void deleteAutorCheck(){
		Autor autor = new Autor();
		autor.setImie(IMIE_1);
		autor.setCountry(COUNTRY_1);
		
		Long AutorId = pM.addAutor(autor);
		
		pM.deleteAutor(autor);
		assertEquals(null, pM.findAutorById(AutorId));
	}
	
	@Test
	public void moveAutorCheck(){
		Autor autor = new Autor();
		autor.setImie(IMIE_1);
		autor.setCountry(COUNTRY_1);
		
		Long AutorId = pM.addAutor(autor);
		
		pM.moveAutor(AutorId, COUNTRY_2);
		
		assertEquals(COUNTRY_2, pM.findAutorById(AutorId).getCountry());
	}

}

