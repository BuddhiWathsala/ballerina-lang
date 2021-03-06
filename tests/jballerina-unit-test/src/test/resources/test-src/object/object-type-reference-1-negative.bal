// Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

type Person1 abstract object {
    public int age;
    public string name;
};

type Employee1 abstract object {
    public float salary;
};

type Manager1 object {
    *Person1;
    *Employee1;

    string dpt = "HR";
};

type EmployeeWithSalary abstract object {
    public float salary;
};

type AnotherEmployeeWithSalary abstract object {
    public int salary;
};

type ManagerWithTwoSalaries object {
    *Person1;

    string dpt = "HR";
    *EmployeeWithSalary;
};

// Test errors for unimplemented methods
type Person2 abstract object {
    public int age;
    public string name;

    public function getName(string? title) returns string;
};

type Employee2 abstract object {
    *Person2;
    public float salary;

    // Unimplemented function at the referenced type.
    public function getSalary() returns float;
};

type Manager2 object {
    string dpt = "HR";
    *Employee2;

    public function getName(string? title) returns string {
        return self.name;
    }

    public function getSalary() returns float {
        return self.salary;
    }
};
