# Web组态-后端源码

## 一、使用方法

1. 获取源码后，修改文件夹名称为 `fastbee-scada` ，然后放置于项目的 `wumei-smart/sprintboot` 文件夹下

   ```
   完整路径: /wumei-smart/sprintboot/fastbee-scada
   ```

2. 执行sql文件时，建议先**比对**一下自己系统的数据（防止已有的会重复添加），然后自己整理出一份新的sql文件执行

3. 图库文件夹（avatar）需要上传至服务器才可使用，因为文件夹太大，没有放在代码目录里，购买后单独发

   **注意**：在服务器上/var/data/java/uploadPath目录下找到已有的avatar文件夹，如果没有，可直接将avatar文件夹上传至此目录下；如果已存在（**建议先备份**），然后需要自己比对两份文件夹内容，合并出新的一份avatar文件夹，然后再上传，**切勿直接替换，不然会把已有的文件弄没了**

## 二、整合到项目

上述组态模块部分代码依赖其他模块的方法，需要按照以下步骤去配置（以下代码已提交到2.2版本里面）

**注意**：情况一：拥有FastBee仓库权限，拉一下仓库最新代码，然后参照以下步骤，只需把pom依赖配置代码注释放开就行

​           情况二：没有FastBee仓库权限，参照以下步骤，把相关代码复制到相关文件里

1. 修改pom依赖配置，刷新maven依赖，重新加载项目

   - 在/wumei-smart/sprintboot/pom.xml配置文件里添加以下配置

     ```
     // 提示：以下代码加在<modules> </modules> 里面
     <module>fastbee-scada</module>
     ```

     ```
     // 提示：以下代码加在<dependencies> </dependencies> 里面
     <!-- 组态模块 -->
     <dependency>
         <groupId>com.fastbee</groupId>
         <artifactId>fastbee-scada</artifactId>
         <version>${fastbee.version}</version>
     </dependency>
     ```

   - 在/wumei-smart/sprintboot/fastbee-admin/pom.xml配置文件里添加以下配置

     ```
     // 提示：以下加在<dependencies> </dependencies>  里面
     <!-- 组态模块 -->
     <dependency>
         <groupId>com.fastbee</groupId>
         <artifactId>fastbee-scada</artifactId>
     </dependency>
     ```

2. 搜索以下三个文件，分别添加代码，已存在可忽略

   ```
   // 1、DeviceLog文件
       private List<String> identityList;
   
       public List<String> getIdentityList() {
           return identityList;
       }
   
       public void setIdentityList(List<String> identityList) {
           this.identityList = identityList;
       }
       
   // 2、FunctionLog文件
   private List<String> identifyList;
   
   // EventLog文件
   private List<String> identityList;
   
       public List<String> getIdentityList() {
           return identityList;
       }
   
       public void setIdentityList(List<String> identityList) {
           this.identityList = identityList;
       }
   ```

3. 在以下文件里分别添加代码，查询属性物模型历史数据

   ```
   // 搜索IDeviceLogService文件
   /**
        * 查询物模型历史数据
        * @param deviceLog 设备日志
        * @return java.util.List<com.fastbee.iot.model.HistoryModel>
        */
       List<HistoryModel> listHistory(DeviceLog deviceLog);
    
   // 搜索DeviceLogServiceImpl文件
       @Override
       public List<HistoryModel> listHistory(DeviceLog deviceLog) {
           return logService.listHistory(deviceLog);
       }
   
   // 搜索ILogService文件
       /**
        * 查询物模型历史数据
        * @param deviceLog 设备日志
        * @return java.util.List<com.fastbee.iot.model.HistoryModel>
        */
       List<HistoryModel> listHistory(DeviceLog deviceLog);
   
   // 搜索MySqlLogServiceImpl文件
       @Override
       public List<HistoryModel> listHistory(DeviceLog deviceLog) {
           return deviceLogMapper.listHistory(deviceLog);
       }
   
   // 搜索TdengineLogServiceImpl文件
       @Override
       public List<HistoryModel> listHistory(DeviceLog deviceLog) {
           // 8时区问题
   //        if (StringUtils.isNotEmpty(deviceLog.getBeginTime())) {
   //            LocalDateTime localDateTime = DateUtils.toLocalDateTime(deviceLog.getBeginTime(), DateUtils.YYYY_MM_DD_HH_MM_SS);
   //            deviceLog.setBeginTime(DateUtils.localDateTimeToStr(localDateTime.minusHours(8), DateUtils.YYYY_MM_DD_HH_MM_SS));
   //        }
   //        if (StringUtils.isNotEmpty(deviceLog.getEndTime())) {
   //            LocalDateTime localDateTime = DateUtils.toLocalDateTime(deviceLog.getEndTime(), DateUtils.YYYY_MM_DD_HH_MM_SS);
   //            deviceLog.setEndTime(DateUtils.localDateTimeToStr(localDateTime.minusHours(8), DateUtils.YYYY_MM_DD_HH_MM_SS));
   //        }
           return tdDeviceLogDAO.listHistory(dbName, deviceLog);
       }
   
   // 搜索DeviceLogMapper文件
   
       /**
        * 查询物模型历史数据
        * @param deviceLog 设备日志
        * @return java.util.List<com.fastbee.iot.model.HistoryModel>
        */
       List<HistoryModel> listHistory(DeviceLog deviceLog);
   
   // 搜索DeviceLogMapper.xml文件
       <select id="listHistory" parameterType="com.fastbee.iot.domain.DeviceLog" resultMap="HistoryResult">
           select log_value,
           create_time,
           identity
           from iot_device_log
           <where>
               <if test="serialNumber != null and serialNumber != ''">
                   and serial_number = #{serialNumber}
               </if>
               <if test="beginTime != null and beginTime != '' and endTime != null and endTime != ''">
                   and create_time between #{beginTime} and #{endTime}
               </if>
               <if test="identityList != null and identityList != ''">
                   and identity in
                   <foreach collection="identityList" item="identity" open="(" separator="," close=")">
                       #{identity}
                   </foreach>
               </if>
           </where>
           order by create_time desc
       </select>
   
   // 搜索TDDeviceLogDAO文件
   List<HistoryModel> listHistory(@Param("database")  String database, @Param("device") DeviceLog deviceLog);
   
   // 搜索TDDeviceLogMapper.xml文件
   <select id="listHistory" parameterType="com.fastbee.iot.domain.DeviceLog" resultMap="HistoryResult">
           select log_value,
           ts,
           identity
           from ${database}.device_log
           <where>
               <if test="device.beginTime != null and device.beginTime != '' and device.endTime != null and device.endTime != ''">
                   and ts between #{device.beginTime} and #{device.endTime}
               </if>
               <if test="device.serialNumber != null and device.serialNumber !=''">
                   and serial_number = #{device.serialNumber}
               </if>
               <if test="device.identityList != null and device.identityList.size > 0">
                   and identity in
                   <foreach collection="device.identityList" item="identity" open="(" separator="," close=")">
                       #{identity}
                   </foreach>
               </if>
           </where>
           order by ts desc
       </select>
   ```

## 三、运行代码

