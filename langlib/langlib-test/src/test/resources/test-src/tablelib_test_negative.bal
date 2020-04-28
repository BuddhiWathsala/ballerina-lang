// Copyright (c) 2020 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

type Person record {
  readonly string name;
  int age;
};

type Employee record {|
  readonly string name;
  string department;
|};

type EmployeeValue record {|
   Employee value;
|};

type PersonalTable table<Person> key(name);

type EmployeeTable table<Employee> key(name);

PersonalTable tab = table key(name)[
  { name: "Chiran", age: 33 },
  { name: "Mohan", age: 37 },
  { name: "Gima", age: 38 },
  { name: "Granier", age: 34 }
];

function getPersonList() returns Person[] {
    Person[] personList = [];
    Person personA = { name: "Chiran", age: 33 };
    Person personB = { name: "Mohan", age: 37 };
    Person personC = { name: "Gima", age: 38 };
    Person personD = { name: "Granier", age: 34 };

    personList.push(personA);
    personList.push(personB);
    personList.push(personC);
    personList.push(personD);

    return personList;
}

function getEmployee(record {| Employee value; |}? returnedVal) returns Employee? {
    if (returnedVal is EmployeeValue) {
        return returnedVal.value;
    } else {
        return ();
    }
}

function testFilterNegative() returns boolean {
    EmployeeTable  filteredTable = tab.filter(function (Employee employee) returns boolean {
                                                 return employee.name == "Chiran";
                                             });
    return filteredTable.length() == 1;
}

function testIteratorNegative() returns boolean {
    boolean testPassed = true;
    Person[] personList = getPersonList();
    abstract object { public function next() returns record {| Employee value; |}?;} itr = tab.iterator();
    Employee? emp = getEmployee(itr.next());
    testPassed = testPassed && emp?.name == personList[0].name;
    return testPassed;
}