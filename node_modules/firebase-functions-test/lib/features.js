"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.features = void 0;
const main_1 = require("./main");
const analytics = require("./providers/analytics");
const auth = require("./providers/auth");
const database = require("./providers/database");
const firestore = require("./providers/firestore");
const pubsub = require("./providers/pubsub");
const storage = require("./providers/storage");
exports.features = {
    mockConfig: main_1.mockConfig,
    wrap: main_1.wrap,
    makeChange: main_1.makeChange,
    analytics,
    auth,
    database,
    firestore,
    pubsub,
    storage,
};
