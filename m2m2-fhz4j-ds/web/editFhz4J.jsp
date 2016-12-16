<%--
    Mango - Open Source M2M - http://mango.serotoninsoftware.com
    Copyright (C) 2009 Arne Plöse.
    @author Arne Plöse

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
--%>
<%@include file="/WEB-INF/jsp/include/tech.jsp" %>

<script type="text/javascript">
    var deviceInfo;

    /**
     * called from init()
     */
    function initImpl() {
    }

    function assembleDevice() {
        dwr.util.removeAllRows("fhz4jValues");
        DataSourceFhz4JEditDwr.getFhz4JProperties($get("deviceType"), assembleDeviceCB);
    }

    function assembleDeviceCB(propertyLabels) {
        dwr.util.addRows("fhz4jValues", propertyLabels, [
            function (propertyLabel) {
                return $get(deviceHousecode);
            },
            function (propertyLabel) {
                return $get(deviceLocation);
            },
            function (propertyLabel) {
                return $get(deviceType);
            },
            function (propertyLabel) {
                return propertyLabel;
            },
            function (propertyLabel) {
                return writeImage("scanDeviceImg_" + $get(fhzHousecode) + "_" + $get(deviceType) + "_" + propertyLabel, null, "icon_comp_add",
                        "<fmt:message key="common.add"/>", "addPoint({'fhzHousecode': '" + $get(fhzHousecode) + "', 'deviceLocation': '" + $get(deviceLocation) + "', 'deviceType': '" + $get(deviceType) + "', 'propertyLabel': '" + propertyLabel + "'})");
            }
        ],
                {
                    rowCreator: function (options) {
                        var tr = document.createElement("tr");
                        tr.className = "row" + (options.rowIndex % 2 == 0 ? "" : "Alt");
                        return tr;
                    }
                });
    }

    function saveDataSourceImpl() {
        Fhz4JEditDwr.saveFhz4JDataSource($get("dataSource.name"),
                $get("dataSource.xid"), $get("commPort"),
                $get("fhzHousecode"), $get("fhtMaster"), saveDataSourceCB);
    }

    function appendPointListColumnFunctions(pointListColumnHeaders, pointListColumnFunctions) {
        pointListColumnHeaders[pointListColumnHeaders.length] = "<fmt:message key='dsEdit.fhz4j.fhtHousecode'/>";
        pointListColumnFunctions[pointListColumnFunctions.length] = function (p) {
            return p.pointLocator.housecodeStr;
        };
    }


    /*
     * indicies passed from addPoint(indicies)
     */
    function addPointImpl(indicies) {
        Fhz4JEditDwr.addFhz4JPoint(indicies.fhzHousecode, indicies.deviceLocation, indicies.deviceType, indicies.propertyLabel, editPointCB);
    }


    function editPointCBImpl(locator) {
        $set("housecode", locator.housecodeStr);
    }

    function savePointImpl(locator) {
        //TODO avoid double settings use annotations of dwr 3.0+ ?
        delete locator.fhzHousecode;
        delete locator.fhzProperty;
        delete locator.fhzDeviceType;
        locator.fhzPropertyLabel = $get("editFhzPropertyLabel");
        locator.fhzDeviceTypeLabel = $get("editDeviceTypeLabel");
        locator.fhzHousecodeStr = $get("FhzHousecode");
        locator.settable = $get("editDataPointSettable");

        Fhz4JEditDwr.saveFhz4JPointLocator(currentPoint.id, $get("xid"), $get("name"), locator, savePointCB);
    }

</script>

<tag:dataSourceAttrs descriptionKey="dsEdit.fhz4j.desc" helpId="fhz4jDS">
    <jsp:body>

        <tr>
            <td class="formLabelRequired"><fmt:message key="dsEdit.serial.port" /></td>
            <td class="formField">
                <c:choose>
                    <c:when test="${!empty commPortError}">
                        <input id="commPort" type="hidden" value="" />
                        <span class="formError">${commPortError}</span>
                    </c:when>
                    <c:otherwise>
                        <sst:select id="commPort" value="${dataSource.commPort}">
                            <c:forEach items="${commPorts}" var="port">
                                <sst:option value="${port.name}">${port.name}</sst:option>
                            </c:forEach>
                            <sst:option value="/dev/ttyACM0">/dev/ttyACM0</sst:option>
                            <sst:option value="/dev/ttyACM1">/dev/ttyACM1</sst:option>
                            <sst:option value="/dev/ttyACM2">/dev/ttyACM2</sst:option>
                            <sst:option value="/dev/ttyACM3">/dev/ttyACM3</sst:option>
                        </sst:select>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>

        <tr>
            <td class="formLabelRequired"><fmt:message key="dsEdit.fhz4j.fhzHouseCode"/></td>
            <td class="formField">
                <input type="text" id="fhzHousecode" value="${dataSource.fhzHousecodeStr}" class="formShort"/>
            </td>
        </tr>

        <tr>
            <td colspan="2" />
        <input type="checkbox" id="fhtMaster" <c:if test="${dataSource.fhtMaster}"> checked="checked"</c:if> class="formShort" ><fmt:message key="dsEdit.fhz4j.fhtMaster"/></input>
        </td>
    </tr>
</jsp:body>

</tag:dataSourceAttrs>

<tag:pointList pointHelpId="fhz4jPP">
        <tr>
        <td class="formLabelRequired"><fmt:message key="dsEdit.fhz4j.fhtHousecode"/></td>
        <td class="formField"><input type="text" id="housecode"/></td>
    </tr>
</tag:pointList>
