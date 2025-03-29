<template>
  <div id="mapContainer"></div>
</template>

<script>
import { listDeviceByRole } from "@/api/device/device";

export default {
  name: "AmapDeviceMonitor",
  data() {
    return {
      map: null,
      markers: [],
      deviceList: []
    };
  },
  async mounted() {
    try {
      await this.initAMap();
      await this.loadDeviceData();
    } catch (error) {
      console.error("初始化失败:", error);
    }
  },
  beforeDestroy() {
    if (this.map) {
      this.map.destroy();
    }
  },
  methods: {
    async loadDeviceData() {
      try {
        const res = await listDeviceByRole({
          pageSize: 1000,
          roleName: this.$store.state.user.roleName,
          deptName: this.$store.state.user.deptName || '陕西省'
        });

        this.deviceList = res.rows
          .map(ele => ({
            id: ele.deviceId,
            name: ele.province,
            status: ele.status,
            coordinates: ele.coordinates.split(",").map(Number).reverse(),
          }))
          .filter(ele => ele.coordinates[0] > 0);

        this.addDeviceMarkers();
      } catch (error) {
        console.error("加载设备数据失败:", error);
        this.useDefaultData();
      }
    },

    useDefaultData() {
      this.deviceList = [
        { id: 1, name: "北京设备1", coordinates: [116.4074, 39.9042], status: "正常" },
        { id: 2, name: "上海设备1", coordinates: [121.4737, 31.2304], status: "正常" },
        { id: 3, name: "上海设备2", coordinates: [121.4737, 31.2304], status: "警告" },
        { id: 4, name: "广东设备1", coordinates: [113.2644, 23.1291], status: "正常" },
        { id: 5, name: "广东设备2", coordinates: [113.2644, 23.1291], status: "故障" },
        { id: 6, name: "广东设备3", coordinates: [113.2644, 23.1291], status: "正常" }
      ];
      this.addDeviceMarkers();
    },

    initAMap() {
      return new Promise((resolve) => {
        if (window.AMap) {
          this.setupAMap();
          resolve();
          return;
        }

        const script = document.createElement('script');
        script.src = `https://webapi.amap.com/maps?v=2.0&key=e33e07fe78568a78b48e3c4b8b1551e4`;
        script.onload = () => {
          this.setupAMap();
          resolve();
        };
        document.head.appendChild(script);
      });
    },

    setupAMap() {
      this.map = new AMap.Map('mapContainer', {
        viewMode: '3D',
        zoom: 11,
        center: [116.397428, 39.90923],
        pitch: 45,
        rotation: -15,
        showBuildingBlock: true,
        mapStyle: 'amap://styles/normal',
        // mapStyle: 'amap://styles/light',
      });
    },

    addDeviceMarkers() {
      if (!this.map) {
        console.error('地图尚未初始化');
        return;
      }

      if (this.markers.length) {
        this.map.remove(this.markers);
        this.markers = [];
      }

      this.deviceList.forEach(device => {
        const statusMap = {'正常': 'normal', '警告': 'warning', '故障': 'error'};
        const status = statusMap[device.status] || 'normal';
        const colorMap = {
          'normal': "#00ff00",
          'warning': "#ff9900",
          'error': "#ff0000"
        };

        if (!device.coordinates || device.coordinates.length !== 2) return;

        const marker = new AMap.Marker({
          position: device.coordinates,
          content: this.getMarkerContent(device.name, colorMap[status]),
          offset: new AMap.Pixel(-12, -12)
        });

        marker.on('click', (e) => {
          this.showInfoWindow(device, colorMap[status], e);
        });

        this.markers.push(marker);
      });

      if (this.markers.length) {
        this.map.add(this.markers);
        this.map.setFitView(this.markers);
      }
    },

    getMarkerContent(name, color) {
      return `
        <div style="
          width: 24px;
          height: 24px;
          border-radius: 50%;
          border: 2px solid white;
          background-color: ${color};
          box-shadow: 0 0 10px ${color};
          position: relative;
          color: ${color};
        ">
          <div style="
            position: absolute;
            top: -20px;
            left: 50%;
            transform: translateX(-50%);
            background: white;
            padding: 2px 5px;
            border-radius: 3px;
            font-size: 12px;
            white-space: nowrap;
            color: #333;
          ">${name}</div>
        </div>
      `;
    },

    showInfoWindow(device, color, event) {
      const statusMap = {'正常': 'normal', '警告': 'warning', '故障': 'error'};
      const status = statusMap[device.status] || 'normal';

      new AMap.InfoWindow({
        content: `
          <div style="padding: 5px; min-width: 120px">
            <h3 style="margin: 5px 0">${device.name}</h3>
            <p>状态: <span style="color:${color}">
              ${status.toUpperCase()}
            </span></p>
            <p>坐标: ${device.coordinates.join(', ')}</p>
            <p>设备ID: ${device.id}</p>
          </div>
        `,
        offset: new AMap.Pixel(0, -30)
      }).open(this.map, event.target.getPosition());
    }
  }
};
</script>

<style scoped>
#mapContainer {
  width: 100%;
  height: 100vh;
  background-color: #f0f2f5;
}
</style>
