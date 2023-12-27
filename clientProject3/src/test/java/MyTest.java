import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MyTest {

	@Test
	void test() {
		fail("Not yet implemented");
	}

	@Test
	void gameDataConstructorTest(){
		GameData test = new GameData();
		assertNotNull(test, "data object not created");
		for (int i: test.currentWord) {
			assertEquals(0, i, "array not init to 0");
		}
	}

	@Test
	void gameDataSetWordTest(){
		GameData test = new GameData();
		test.setWord("tyler");
		assertEquals("tyler", test.word, "setWord not working");

		test.setWord("cookie");
		assertEquals("cookie", test.word, "setWord not working");
	}

	@Test
	void gameDataSetCategoryTest(){
		GameData test = new GameData();
		test.setCategory("meatballs");
		assertEquals("meatballs", test.category, "setWord not working");

		test.setWord("countries");
		assertEquals("countries", test.category, "setWord not working");
	}

	@Test
	void gameDataResetWordTest(){
		GameData test = new GameData();
		test.resetWord();

		for (int i: test.currentWord) {
			assertEquals(0, i, "array not set to 0");
		}
	}

	@Test
	void gameDataSetGuessesTest(){
		GameData test = new GameData();
		test.setGuesses(3);
		assertEquals(3, test.guessesLeft, "guesses not set correctly");

		test.setGuesses(6);
		assertEquals(6, test.guessesLeft, "guesses not set correctly");
	}
}
