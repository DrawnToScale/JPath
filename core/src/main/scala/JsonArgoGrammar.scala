package com.gu.jpath

import scala.util.parsing.combinator._

object JsonArgoGrammar extends RegexParsers {

	def query = repsep(queryToken, ".")
	
	def queryToken = wholeArrayQueryToken | arrayAccessQueryToken | directAccessQueryToken
	
	def wholeArrayQueryToken = identifier <~ "[*]" ^^ { id => QueryToken.all(id) }
	
	def arrayAccessQueryToken = identifier ~ arrayIndex ^^ { 
		case ident ~ index => QueryToken(ident, index)
	} 
	
	def directAccessQueryToken = identifier ^^ { id =>  QueryToken(id) }
	
	def arrayIndex = "[" ~> digits <~ "]"
	
	def identifier = """[a-zA-Z_-]+""".r 
	
	def digits = """\d+""".r ^^ { _.toInt }
	
	def tokens(path: String) : List[QueryToken]	=
		parseAll(query, path) match {
			case Success(tokens, _) => tokens
			case _ => throw new IllegalArgumentException("Invalid path: " + path)
		}
}
