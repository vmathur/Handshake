from tornado.ioloop import IOLoop
from tornado.web import RequestHandler, Application, url
import pycps
import json

class ConnectionRegisterHandler(RequestHandler):
	def post(self):
		user_id = self.get_argument('user_id')
		devices = self.get_argument('devices')
		session_db = pycps.Connection('tcp://cloud-us-0.clusterpoint.com:9007', 'sessions', 'vmmonkey@gmail.com', 'monkey', '100642')

		try:
			response = session_db.retrieve(devices)
		except pycps.APIError as e:
			print(e)
			if e.code == 2824:
				print("Requested non-existing id(s): {0}".format(', '.join(e.document_id)))
			return

		try:
			if response.get_documents:
				user_id = response.get_documents().items()[0]
				session_db.delete(user_id)
				self.write('success')
				self.finish()
			else:
				session_db.insert({user_id : {
						'devices' : devices
					}
				})
				self.finish()
		except pycps.APIError as e:
			return
