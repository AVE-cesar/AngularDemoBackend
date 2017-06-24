$output.resource("readme.txt")##

#########################################################################################
## macro that can generate various useful String to deal with composite key:  
## str1: {keyPart1},{keyPart2}
## str2: {keyPart1: $stateParams.keyPart1, keyPart2: $stateParams.keyPart2}
## str3: @PathVariable String keyPart1, @PathVariable String keyPart2
## str4: keyPart1, keyPart2
## str5: String keyPart1, String keyPart2
## str6: :keyPart1,:keyPart2
## str7: {{item.id.keyPart1}}:{{item.id.keyPart2}}
## str8: {keyPart1: item.id.keyPart1, keyPart2: item.id.keyPart2}
## str9: { "column1": true, "column2": true, ... }
## str10: column1, column2, ... 
#########################################################################################
##
## Be careful: this method below is call several times; be sure to change all calls when you add a parameter
## calls: 
##			3 inside this file
##			1 in EntityController.e.vm.js
##			1 in EntityEditController.e.vm.js
##			1 in entity.e.vm.html
##			1 in CompositePk.cpk.vm.java
##			1 in EntityResource.e.vm.java
##	
##
#macro(generateSimpleOrCompositeKeyForURL $str1 $str2 $str3 $str4 $str5 $str6 $str7 $str8 $str9 $str10 $list)
#set ($compositeCall = false) 
	#foreach ($attribute in $list)
		#if ($attribute.isInCpk() == true)
			#if ($str1 == "")
				#set ($str1 = "{$attribute.name}")
				#set ($compositeCall = true)
				#set ($str2 = "{$attribute.name: ${dollar}stateParams.$attribute.name")
				#set ($str3 = "@PathVariable $attribute.type $attribute.var")
				#set ($str4 = "$attribute.var")
				#set ($str5 = "$attribute.type $attribute.var")
				#set ($str6 = ":$attribute.name")
				#set ($str7 = "{{item.id.$attribute.name}}")
				#set ($str8 = "{$attribute.name: item.id.$attribute.name")
			#else
				#set ($str1 = "$str1,{$attribute.name}")
				#set ($str2 = "$str2, $attribute.name: ${dollar}stateParams.$attribute.name")
				#set ($str3 = "$str3, @PathVariable $attribute.type $attribute.var")
				#set ($str4 = "$str4, $attribute.var")
				#set ($str5 = "$str5, $attribute.type $attribute.var")
				#set ($str6 = "$str6,:$attribute.name")
				#set ($str7 = "$str7:{{item.id.$attribute.name}}")
				#set ($str8 = "$str8, $attribute.name: item.id.$attribute.name")
			#end
		#end
		
		#if ($str9 == "") 
			#set ($str9 = "{ \"$attribute.name\": true")
			#set ($str10 = "$attribute.getColumnName()")
		#else
			#set ($str9 = "$str9, \"$attribute.name\": true")
			#set ($str10 = "$str10, $attribute.getColumnName()")
		#end
	#end
	
	#if ($str1 == "")
		#set ($str1 = "{id}") 
		#set ($str2 = "{id : ${dollar}stateParams.id}")
		#set ($str6 = ":id")
	#end	
	#if ($compositeCall == true)
		## close bracket
		#set ($str2 = "$str2}")
		#set ($str8 = "$str8}")
	#end
	## close bracket
	#set ($str9 = "$str9}")
#end
